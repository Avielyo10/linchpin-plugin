import hudson.*;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import util.linchPinUtil;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Aviel
 */
public class linchPinPublisher extends Publisher implements SimpleBuildStep {
    private String installation;

    @DataBoundConstructor
    public linchPinPublisher() {}

    public String getInstallation() {
        return installation;
    }

    @DataBoundSetter
    public void setInstallation(String installation) {
        this.installation = Util.fixEmpty(installation);
    }
    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) throws InterruptedException, IOException {
        linchPinUtil util = new linchPinUtil();
        if(util.readTmp() == null){
            throw new AbortException("You might want to use LinchPin first.");
        }

        util.toCmd(workspace + "", "bin/linchpin destroy",launcher,listener);
        util.toCmd("","rm /tmp/linchpin.out",launcher,listener);
    }


    @Extension @Symbol("linchPinTearDown")
    public static class DescriptorImp extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "LinchPin TearDown";
        }
    }
}
