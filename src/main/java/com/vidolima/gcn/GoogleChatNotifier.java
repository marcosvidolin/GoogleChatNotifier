package com.vidolima.gcn;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

/**
 * {@link Publisher} that sends the build result in Google Chat.
 *
 * @author Marcos A. Vidolin de Lima
 */
public class GoogleChatNotifier extends Notifier implements SimpleBuildStep {

    protected static final Logger LOGGER = Logger.getLogger(GoogleChatNotifier.class.getName());

    private String webhookUrl;
    private boolean considerBuildStatus;

    /**
     * @param webhookUrl
     * @param considerBuildStatus
     */
    @DataBoundConstructor
    public GoogleChatNotifier(String webhookUrl, boolean considerBuildStatus) {
        this.webhookUrl = webhookUrl;
        this.considerBuildStatus = considerBuildStatus;
    }

    @DataBoundSetter
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookUrl() {
        return this.webhookUrl;
    }

    @DataBoundSetter
    public void setConsiderBuildStatus(boolean considerBuildStatus) {
        this.considerBuildStatus = considerBuildStatus;
    }

    public boolean getConsiderBuildStatus() {
        return this.considerBuildStatus;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) {
        try {
            listener.getLogger().println("Sending Google Chat notification");
            listener.getLogger().println(run.getResult()); // TODO: remove?
            processBuildEvent(run);
        } catch (Exception e) {
            listener.getLogger().printf("Error Occurred : %s ", new Object[] { e });
        }
        listener.getLogger().println("Google Chat notification set!");
    }

    /**
     * This class does explicit check pointing.
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Symbol({"googleChatNotifier"})
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public FormValidation doCheckName(@QueryParameter String value) {
            if (value.length() == 0) {
//                return FormValidation.error(Messages.GoogleChatNotifierBuilder_DescriptorImpl_errors_missingWebhookUrl());
                return FormValidation.error(""); // TODO
            }
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            //return Messages.GoogleChatNotifierBuilder_DescriptorImpl_DisplayName();
            return "getDisplayName"; // TODO
        }
    }

    private boolean isSuccess(Result buildResult) {
        if (buildResult == null) {
            return true;
        }
        if (buildResult == Result.SUCCESS) {
            return true;
        }
        if ((buildResult == Result.UNSTABLE) && (getConsiderBuildStatus())) {
            return true;
        }
        if (buildResult == Result.ABORTED) {
            return true;
        }
        if (buildResult.equals(Result.NOT_BUILT)) {
            return true;
        }
        return false;
    }

    private void processBuildEvent(Run<?, ?> run) throws IOException {
        boolean isSuccess = isSuccess(run.getResult());

        String textParagraph = isSuccess ? "Well done keep it up!" : "Please check it and try again.";
        String statusMessage = isSuccess ? " success" : " failure";

        GoogleChatMessage message = new GoogleChatMessage.Builder().headerTitle(run.getExternalizableId()).headerSubtitle("Build #" + run.getNumber() + statusMessage).considerBuildStatus(getConsiderBuildStatus()).textParagraph(textParagraph).isBuildSuccessful(isSuccess).openLinkUrl(JenkinsUtil.buildUrl(run.getUrl())).build();

        NotifierClient notifier = new NotifierClient.Builder().webHookUrl(getWebhookUrl()).message(message.toJson()).build();

        notifier.sendNotification();
    }
}