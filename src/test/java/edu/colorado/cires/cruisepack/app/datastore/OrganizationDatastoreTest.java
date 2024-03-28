package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationData;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OrganizationDatastoreTest extends OverridableXMLDatastoreTest<OrganizationData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final OrganizationDatastore datastore = new OrganizationDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .map(o -> new NameUUIDPair(o.getUuid(), o.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        OrganizationDatastore.UNSELECTED_ORGANIZATION.getId(), OrganizationDatastore.UNSELECTED_ORGANIZATION.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getAllOrganizationDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getEnabledOrganizationDropDowns() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(Organization::isUse)
        .map(o -> new NameUUIDPair(o.getUuid(), o.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        OrganizationDatastore.UNSELECTED_ORGANIZATION.getId(), OrganizationDatastore.UNSELECTED_ORGANIZATION.getValue()
    ));

    Set<NameUUIDPair> actual = datastore.getEnabledOrganizationDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());

    assertEquals(expected, actual);
  }

  @Test
  void saveOrganization() {
    datastore.init();
    
    Organization organization = createOrganization("1", true);
    
    datastore.save(organization);

    Optional<DropDownItem> maybeSavedItem = datastore.getEnabledOrganizationDropDowns().stream()
        .filter(d -> d.getId().equals(organization.getUuid()) && d.getValue().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedItem.isPresent());

    Optional<Organization> maybeSavedOrganization = readFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(p -> p.getUuid().equals(organization.getUuid()) && p.getName().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedOrganization.isEmpty()); // should not be in default data file

    maybeSavedOrganization = readLocalFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(p -> p.getUuid().equals(organization.getUuid()) && p.getName().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedOrganization.isPresent()); // should be stored in overrides file

    Organization savedOrganization = maybeSavedOrganization.get();
    assertOrganizationsEqual(organization, savedOrganization);
  }

  @Test
  void saveOrganizationOverride() {
    datastore.init();

    Organization organization = datastore.findByUUID("01082b23-154c-4c2d-bef7-5e35315d41e0").orElseThrow(
        () -> new IllegalStateException("Organization not found")
    );
    assertTrue(organization.isUse());
    organization.setUse(false);

    datastore.save(organization);

    Optional<DropDownItem> maybeSavedItem = datastore.getAllOrganizationDropDowns().stream()
        .filter(d -> d.getId().equals(organization.getUuid()) && d.getValue().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedItem.isPresent());
    
    assertTrue(
        datastore.getEnabledOrganizationDropDowns().stream()
            .filter(d -> d.getId().equals(organization.getUuid()) && d.getValue().equals(organization.getName()))
            .findFirst().isEmpty()
    );

    Optional<Organization> maybeSavedOrganization = readFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(p -> p.getUuid().equals(organization.getUuid()) && p.getName().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedOrganization.isPresent()); // should be present in default file without edits
    assertTrue(maybeSavedOrganization.get().isUse());

    maybeSavedOrganization = readLocalFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(p -> p.getUuid().equals(organization.getUuid()) && p.getName().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedOrganization.isPresent()); // should be stored in overrides file with edits
    assertFalse(maybeSavedOrganization.get().isUse());

    Organization savedOrganization = maybeSavedOrganization.get();
    assertOrganizationsEqual(organization, savedOrganization);
  }

  @Test
  void saveModel() {
    datastore.init();

    OrganizationModel organization = createModel("1", true);

    datastore.save(organization);

    Optional<DropDownItem> maybeSavedItem = datastore.getEnabledOrganizationDropDowns().stream()
        .filter(d -> d.getId().equals(organization.getUuid()) && d.getValue().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedItem.isPresent());

    Optional<Organization> maybeSavedOrganization = readFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(p -> p.getUuid().equals(organization.getUuid()) && p.getName().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedOrganization.isEmpty()); // should not be in default data file

    maybeSavedOrganization = readLocalFile(OrganizationData.class).getOrganizations().getOrganizations().stream()
        .filter(p -> p.getUuid().equals(organization.getUuid()) && p.getName().equals(organization.getName()))
        .findFirst();
    assertTrue(maybeSavedOrganization.isPresent()); // should be stored in overrides file

    Organization savedOrganization = maybeSavedOrganization.get();
    assertOrganizationEqualsOrganizationModelEqual(organization, savedOrganization);
  }

  @Test
  void getByUUID() {
    datastore.init();

    String uuid = "01082b23-154c-4c2d-bef7-5e35315d41e0";
    Optional<Organization> maybeOrganization = datastore.findByUUID(uuid);
    assertTrue(maybeOrganization.isPresent());
    assertEquals(uuid, maybeOrganization.get().getUuid());

    assertTrue(datastore.findByUUID("TEST").isEmpty());
  }

  @Test
  void findByName() {
    datastore.init();

    String name = "AFSC";
    Optional<Organization> maybeOrganization = datastore.findByName(name);
    assertTrue(maybeOrganization.isPresent());
    assertEquals(name, maybeOrganization.get().getName());

    assertTrue(datastore.findByName("TEST").isEmpty());
  }

  @Override
  protected OrganizationData createDataObject() {
    OrganizationData data = new OrganizationData();
    data.setDataVersion("1.0");
    data.setOrganizations(new OrganizationList());
    return data;
  }

  @Override
  protected String getXMLFilename() {
    return "organizations.xml";
  }

  private static Organization createOrganization(String suffix, boolean use) {
    Organization organization = new Organization();

    organization.setUuid(String.format("uuid-%s", suffix));
    organization.setName(String.format("organization-%s", suffix));
    organization.setStreet(String.format("street-%s", suffix));
    organization.setCity(String.format("city-%s", suffix));
    organization.setState(String.format("state-%s", suffix));
    organization.setZip(String.format("zip-%s", suffix));
    organization.setCountry(String.format("country-%s", suffix));
    organization.setPhone(String.format("phone-%s", suffix));
    organization.setEmail(String.format("email-%s", suffix));
    organization.setUse(use);

    return organization;
  }

  private void assertOrganizationsEqual(Organization expected, Organization actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.isUse(), actual.isUse());
  }

  private static OrganizationModel createModel(String suffix, boolean use) {
    OrganizationModel organizationModel = new OrganizationModel();

    organizationModel.setUuid(String.format("uuid-%s", suffix));
    organizationModel.setName(String.format("organization-%s", suffix));
    organizationModel.setStreet(String.format("street-%s", suffix));
    organizationModel.setCity(String.format("city-%s", suffix));
    organizationModel.setState(String.format("state-%s", suffix));
    organizationModel.setZip(String.format("zip-%s", suffix));
    organizationModel.setCountry(String.format("country-%s", suffix));
    organizationModel.setPhone(String.format("phone-%s", suffix));
    organizationModel.setEmail(String.format("email-%s", suffix));
    organizationModel.setUse(use);

    return organizationModel;
  }

  private void assertOrganizationEqualsOrganizationModelEqual(OrganizationModel expected, Organization actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.isUse(), actual.isUse());
  }
}