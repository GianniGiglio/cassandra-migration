package com.contrastsecurity.cassandra.migration.utils.scanner.classpath;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsgiClassPathLocationScanner implements ClassPathLocationScanner {

    private static final Pattern bundleIdPattern = Pattern.compile("^\\d+");

    public Set<String> findResourceNames(String location, URL locationUrl) throws IOException {
        Set<String> resourceNames = new TreeSet<String>();

        Bundle bundle = getTargetBundleOrCurrent(FrameworkUtil.getBundle(getClass()), locationUrl);
        @SuppressWarnings({"unchecked"})
        Enumeration<URL> entries = bundle.findEntries(locationUrl.getPath(), "*", true);

        if (entries != null) {
            while (entries.hasMoreElements()) {
                URL entry = entries.nextElement();
                String resourceName = getPathWithoutLeadingSlash(entry);

                resourceNames.add(resourceName);
            }
        }

        return resourceNames;
    }

    private Bundle getTargetBundleOrCurrent(Bundle currentBundle, URL locationUrl) {
        try {
            Bundle targetBundle = currentBundle.getBundleContext().getBundle(getBundleId(locationUrl.getHost()));
            return targetBundle != null ? targetBundle : currentBundle;
        } catch (Exception e) {
            return currentBundle;
        }
    }

    private long getBundleId(String host) {
        final Matcher matcher = bundleIdPattern.matcher(host);
        if (matcher.find()) {
            return Double.valueOf(matcher.group()).longValue();
        }
        throw new IllegalArgumentException("There's no bundleId in passed URL");
    }

    private String getPathWithoutLeadingSlash(URL entry) {
        final String path = entry.getPath();

        return path.startsWith("/") ? path.substring(1) : path;
    }
}