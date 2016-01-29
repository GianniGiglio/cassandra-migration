package com.contrastsecurity.cassandra.migration.config;

import com.datastax.driver.core.Metadata;

public class Keyspace {
    private static final String PROPERTY_PREFIX = "cassandra.migration.keyspace.";

    public enum KeyspaceProperty {
        NAME(PROPERTY_PREFIX + "name", "Name of Cassandra keyspace");

        private String name;
        private String description;

        KeyspaceProperty(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private Cluster cluster;
    private String name;
    private boolean quoted=false;

    public Keyspace() {
        cluster = new Cluster();
        String keyspaceP = System.getProperty(KeyspaceProperty.NAME.getName());
        if (null != keyspaceP && keyspaceP.trim().length() != 0)
            this.name = keyspaceP;
    }
    
    

    public boolean isQuoted() {
		return quoted;
	}



	public void setQuoted(boolean quoted) {
		this.quoted = quoted;
	}



	public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getName() {
        return isQuoted()?Metadata.quote(name):name;
    }
    
    public String getKeySpaceSystemName(){
    	return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
