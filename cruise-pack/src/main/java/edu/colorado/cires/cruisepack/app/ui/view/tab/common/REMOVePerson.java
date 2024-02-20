package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

public class REMOVePerson {

    private final String name;
    private final String position;
    private final String organization;
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

    

    public REMOVePerson(String name, String position, String organization, String street, String city, String state,
            String zip, String country, String phone, String email, String orcidID, String uuid, Boolean use) {
        this.name = name;
        this.position = position;
        this.organization = organization;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.orcidID = orcidID;
        this.uuid = uuid;
        this.use = use;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", position=" + position + ", organization=" + organization + ", street="
                + street + ", city=" + city + ", state=" + state + ", zip=" + zip + ", country=" + country + ", phone="
                + phone + ", email=" + email + ", orcidID=" + orcidID + ", uuid=" + uuid + ", use=" + use + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String position;
        private String organization;
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

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPosition(String position) {
            this.position = position;
            return this;
        }

        public Builder withOrganization(String organization) {
            this.organization = organization;
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

        public Builder withOrcidID(String orcidID) {
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

        public REMOVePerson build() {
            return new REMOVePerson(
                name, 
                position, 
                organization, 
                street, 
                city, 
                state, 
                zip, 
                country, 
                phone, 
                email, 
                orcidID,
                uuid,
                use
            );
        }
    }
    
}
