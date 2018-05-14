package io.jenkins.plugins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
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

public class GoogleChatNotifier extends Notifier {

    private String googleChatWebhookUrl;

    @DataBoundConstructor
    public GoogleChatNotifier() {
        this("");
    }

    @Deprecated
    public GoogleChatNotifier(final String googleChatWebhookUrl) {
        this.googleChatWebhookUrl = googleChatWebhookUrl;
    }

    @DataBoundSetter
    public void setGoogleChatWebhookUrl(String googleChatWebhookUrl) {
        this.googleChatWebhookUrl = googleChatWebhookUrl;
    }

    public String getGoogleChatWebhookUrl() {
        return this.googleChatWebhookUrl;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(@SuppressWarnings("rawtypes") final AbstractBuild build,
                           final Launcher launcher, final BuildListener listener) {
        try {
            // logger which prints on job 'Console Output'
            listener.getLogger().println("Starting Post Build Action");
            sendNotification();
        } catch (Exception e) {
            listener.getLogger().printf("Error Occurred : %s ", e);
        }
        listener.getLogger().println("Finished Post Build Action");
        return Boolean.TRUE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    private void sendNotification() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(this.getGoogleChatWebhookUrl());

        String json = "{\"cards\": [{\"header\": {\"title\": \"Pizza Bot Customer Support\",\"subtitle\": \"pizzabot@example.com\",\"imageUrl\": \"https://goo.gl/aeDtrS\",\"imageStyle\": \"IMAGE\"}}]}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpPost);
        System.out.println(response.getStatusLine());
        response.close();
    }

    @Symbol("gcn")
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * Global configuration information variables. If you don't want fields
         * to be persisted, use <tt>transient</tt>.
         */
//        private String username;
//        private String password;
//
//        public String getUsername() {
//            return username;
//        }
//
//        public String getPassword() {
//            return password;
//        }

        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

            // To persist global configuration information, set that to
            // properties and call save().
//            username = formData.getString("username");
//            password = formData.getString("password");
            save();
            return super.configure(req, formData);
        }

        @Override
        public boolean isApplicable(
                @SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
            // Indicates that this builder can be used with all kinds of project types.
            return Boolean.TRUE;
        }

        @Override
        public String getDisplayName() {
            return Messages.GoogleChatNotifier_DescriptorImpl_DisplayName();
        }
    }

}
