package com.vidolima.gcn;

public class GoogleChatMessage {

    private static final String DEFAULT_IMAGE_STYLE = "IMAGE";
    private static final String DEFAULT_TEXT_BUTTON = "OPEN SITE";
    private static final String DEFAULT_IMAGE_URL = "https://s3-sa-east-1.amazonaws.com/bts-it-public/jenkins-chat/jenkins.png"; // TODO: remover da CIANDT
    private String headerTitle;
    private String headerSubtitle;
    private String imageUrl;
    private String imageStyle;
    private String textParagraph;
    private String textButton;
    private String openLinkUrl;
    private boolean considerBuildStatus;
    private boolean isBuildSuccessful;

    public String getHeaderTitle() {
        return this.headerTitle;
    }

    public String getHeaderSubtitle() {
        return this.headerSubtitle;
    }

    public String getImageUrl() {
        if (this.imageUrl == null) {
            this.imageUrl = "https://s3-sa-east-1.amazonaws.com/bts-it-public/jenkins-chat/jenkins.png"; // TODO: remover da CIANDT
        }
        return this.imageUrl;
    }

    public String getImageStyle() {
        if (this.imageStyle == null) {
            this.imageStyle = "IMAGE";
        }
        return this.imageStyle;
    }

    public String getTextParagraph() {
        return this.textParagraph;
    }

    public String getTextButton() {
        if (this.textButton == null) {
            this.textButton = "OPEN SITE";
        }
        return this.textButton;
    }

    public String getOpenLinkUrl() {
        return this.openLinkUrl;
    }

    public boolean getConsiderBuildStatus() {
        return this.considerBuildStatus;
    }

    public boolean isBuildSuccessful() {
        return this.isBuildSuccessful;
    }

    private GoogleChatMessage(String headerTitle, String headerSubtitle, String imageUrl, String imageStyle, String textParagraph, String textButton, String openLinkUrl, boolean considerBuildStatus, boolean isBuildSuccessful) {
        this.headerTitle = headerTitle;
        this.headerSubtitle = headerSubtitle;
        this.imageUrl = imageUrl;
        this.imageStyle = imageStyle;
        this.textParagraph = textParagraph;
        this.textButton = textButton;
        this.openLinkUrl = openLinkUrl;
        this.considerBuildStatus = considerBuildStatus;
        this.isBuildSuccessful = isBuildSuccessful;
    }

    public static class Builder {
        private String headerTitle;
        private String headerSubtitle;
        private String imageUrl;
        private String imageStyle;
        private String textParagraph;
        private String textButton;
        private String openLinkUrl;
        private boolean considerBuildStatus;
        private boolean isBuildSuccessful;

        public Builder headerTitle(String headerTitle) {
            this.headerTitle = headerTitle;
            return this;
        }

        public Builder headerSubtitle(String headerSubtitle) {
            this.headerSubtitle = headerSubtitle;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder imageStyle(String imageStyle) {
            this.imageStyle = imageStyle;
            return this;
        }

        public Builder textParagraph(String textParagraph) {
            this.textParagraph = textParagraph;
            return this;
        }

        public Builder textButton(String textButton) {
            this.textButton = textButton;
            return this;
        }

        public Builder openLinkUrl(String openLinkUrl) {
            this.openLinkUrl = openLinkUrl;
            return this;
        }

        public Builder considerBuildStatus(boolean considerBuildStatus) {
            this.considerBuildStatus = considerBuildStatus;
            return this;
        }

        public Builder isBuildSuccessful(boolean isBuildSuccessful) {
            this.isBuildSuccessful = isBuildSuccessful;
            return this;
        }

        public GoogleChatMessage build() {
            return new GoogleChatMessage(this.headerTitle, this.headerSubtitle, this.imageUrl, this.imageStyle, this.textParagraph, this.textButton, this.openLinkUrl, this.considerBuildStatus, this.isBuildSuccessful);
        }
    }

    public String toJson() {
        String imgUrl = getImageUrl();
        if (getConsiderBuildStatus()) {
            if (isBuildSuccessful()) {
                imgUrl = "https://s3-sa-east-1.amazonaws.com/bts-it-public/jenkins-chat/like.png"; // TODO: remover da CIANDT
            } else {
                imgUrl = "https://s3-sa-east-1.amazonaws.com/bts-it-public/jenkins-chat/unlike.png"; // TODO: remover da CIANDT
            }
        }
        return
                "{\n  \"cards\": [\n    {\n      \"header\": {\n        \"title\": \"" + getHeaderTitle() + "\",\n        \"subtitle\": \"" + getHeaderSubtitle() + "\",\n        \"imageUrl\": \"" + imgUrl + "\",\n        \"imageStyle\": \"" + getImageStyle() + "\"\n      },\n      \n      \"sections\": [\n        \n        {\n          \"widgets\": [\n            {\n              \"textParagraph\": {\n                \"text\": \"" + getTextParagraph() + "\"\n              }\n              \n            }\n          ]\n        },\n        {\n          \n          \"widgets\": [\n            {\n             \"buttons\": [\n              {\n                \"textButton\": {\n                  \"text\": \"" + getTextButton() + "\",\n                  \"onClick\": {\n                    \"openLink\": {\n                      \"url\": \"" + getOpenLinkUrl() + "\"\n                    }\n                  }\n                }\n              }\n            ]\n           }\n         ]\n        }\n      ]\n    }\n  ]\n}";
    }
}