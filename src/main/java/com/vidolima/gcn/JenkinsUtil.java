package com.vidolima.gcn;

import jenkins.model.Jenkins;

public final class JenkinsUtil {
    public static String getRootUrl() {
        return Jenkins.getInstance().getRootUrl();
    }
    public static String buildUrl(String path) {
        return getRootUrl() + path;
    }
}

