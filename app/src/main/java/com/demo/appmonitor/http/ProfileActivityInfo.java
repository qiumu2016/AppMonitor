package com.demo.appmonitor.http;

import java.util.List;

public class ProfileActivityInfo{
    private String login;
    private String avatarUrl;
    private String updatedAt;
    private ProfileActivityInfoItem_1 starredRepositories;
    public class ProfileActivityInfoItem_1{
        private List<ProfileActivityInfoItem_2> edges;
        public class ProfileActivityInfoItem_2{
            private String starredAt;
            private ProfileActivityInfoItem_3 node;
            public class ProfileActivityInfoItem_3{
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
            public String getStarredAt() {
                return starredAt;
            }

            public void setStarredAt(String starredAt) {
                this.starredAt = starredAt;
            }

            public ProfileActivityInfoItem_3 getNode() {
                return node;
            }

            public void setNode(ProfileActivityInfoItem_3 node) {
                this.node = node;
            }
        }

        public List<ProfileActivityInfoItem_2> getEdges() {
            return edges;
        }

        public void setEdges(List<ProfileActivityInfoItem_2> edges) {
            this.edges = edges;
        }


    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProfileActivityInfoItem_1 getStarredRepositories() {
        return starredRepositories;
    }

    public void setStarredRepositories(ProfileActivityInfoItem_1 starredRepositories) {
        this.starredRepositories = starredRepositories;
    }
}
