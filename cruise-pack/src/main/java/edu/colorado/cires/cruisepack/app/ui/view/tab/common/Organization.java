package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

public class Organization {
    private final String organizationName;
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final String country;
    private final String phone;
    private final String email;
    private final String orcidID;
    private final String uuid;
    private final Boolean use;
    
    private Organization(String organizationName, String street, String city, String state, String zip, String country,
            String phone, String email, String orcidID, String uuid, Boolean use) {
        this.organizationName = organizationName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.uuid = uuid;
        this.orcidID = orcidID;
        this.use = use;
    }

    @Override
    public String toString() {
        return "Organization [organizationName=" + organizationName + ", street=" + street + ", city=" + city
                + ", state=" + state + ", zip=" + zip + ", country=" + country + ", phone=" + phone + ", email=" + email
                + ", orcidID=" + orcidID + ", uuid=" + uuid + ", use=" + use + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String organizationName;
        private String street;
        private String city;
        private String state;
        private String zip;
        private String country;
        private String phone;
        private String email;
        private String orcidID;
        private String uuid;
        private Boolean use;
        
        private Builder() {}

        public Builder withOrgnaizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public Builder withStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Builder withCountry(String country) {
            this.country = country;
            return this;
        }
        
        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withOrdcidID(String orcidID) {
            this.orcidID = orcidID;
            return this;
        }

        public Builder withUUID(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder withUse(Boolean use) {
            this.use = use;
            return this;
        }

        public Organization build() {
            return new Organization(organizationName, street, city, state, zip, country, phone, email, orcidID, uuid, use);
        }
    }
}