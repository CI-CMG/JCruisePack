package edu.colorado.cires.cruisepack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.cruisepack.data.FileExtension;
import edu.colorado.cires.cruisepack.xml.gravityCorrectionModel.GravityCorrectionModelList;
import edu.colorado.cires.cruisepack.xml.gravityCorrectionModel.GravityCorrectionModel;
import edu.colorado.cires.cruisepack.xml.instrument.*;
import edu.colorado.cires.cruisepack.xml.magneticsCorrectionModel.MagneticsCorrectionModel;
import edu.colorado.cires.cruisepack.xml.magneticsCorrectionModel.MagneticsCorrectionModelList;
import edu.colorado.cires.cruisepack.xml.navigationDatum.NavigationDatum;
import edu.colorado.cires.cruisepack.xml.navigationDatum.NavigationDatumList;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationList;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.person.PersonList;
import edu.colorado.cires.cruisepack.xml.port.Port;
import edu.colorado.cires.cruisepack.xml.port.PortData;
import edu.colorado.cires.cruisepack.xml.person.PersonData;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationData;
import edu.colorado.cires.cruisepack.xml.port.PortList;
import edu.colorado.cires.cruisepack.xml.sea.Sea;
import edu.colorado.cires.cruisepack.xml.sea.SeaData;
import edu.colorado.cires.cruisepack.xml.sea.SeaList;
import edu.colorado.cires.cruisepack.xml.ship.Ship;
import edu.colorado.cires.cruisepack.xml.ship.ShipData;
import edu.colorado.cires.cruisepack.xml.gravityCorrectionModel.GravityCorrectionModelData;
import edu.colorado.cires.cruisepack.xml.magneticsCorrectionModel.MagneticsCorrectionModelData;
import edu.colorado.cires.cruisepack.xml.ship.ShipList;
import edu.colorado.cires.cruisepack.xml.singlebeamVerticalDatum.SinglebeamVerticalDatum;
import edu.colorado.cires.cruisepack.xml.singlebeamVerticalDatum.SinglebeamVerticalDatumList;
import edu.colorado.cires.cruisepack.xml.waterColumnCalibrationState.WaterColumnCalibrationState;
import edu.colorado.cires.cruisepack.xml.waterColumnCalibrationState.WaterColumnCalibrationStateData;
import edu.colorado.cires.cruisepack.xml.singlebeamVerticalDatum.SinglebeamVerticalDatumData;
import edu.colorado.cires.cruisepack.xml.navigationDatum.NavigationDatumData;
import edu.colorado.cires.cruisepack.xml.waterColumnCalibrationState.WaterColumnCalibrationStateList;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class XmlToJson {
    @Test
    public void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Path basePath = Paths.get("/Users/cllo5609/projects/cruise-pack-data/src/main/resources/edu/colorado/cires/cruisepack/data");

        InstrumentData instrumentData = unmarshalData(basePath, InstrumentData.class, "instruments.xml");
        objectMapper.writeValue(basePath.resolve("instruments.json").toFile(), convertInstrumentData(instrumentData));

        PortData portData = unmarshalData(basePath, PortData.class, "ports.xml");
        objectMapper.writeValue(basePath.resolve("ports.json").toFile(), convertPortData(portData));

        PersonData personData = unmarshalData(basePath, PersonData.class, "people.xml");
        objectMapper.writeValue(basePath.resolve("people.json").toFile(), convertPersonData(personData));

        OrganizationData organizationData = unmarshalData(basePath, OrganizationData.class, "organizations.xml");
        objectMapper.writeValue(basePath.resolve("organizations.json").toFile(), convertOrganizationData(organizationData));

        SeaData seaData = unmarshalData(basePath, SeaData.class, "seas.xml");
        objectMapper.writeValue(basePath.resolve("seas.json").toFile(), convertSeaData(seaData));

        ShipData shipData = unmarshalData(basePath, ShipData.class, "ships.xml");
        objectMapper.writeValue(basePath.resolve("ships.json").toFile(), convertShipData(shipData));

        GravityCorrectionModelData gravityCorrectionModelData = unmarshalData(basePath, GravityCorrectionModelData.class, "gravityCorrectionModels.xml");
        objectMapper.writeValue(basePath.resolve("gravityCorrectionModels.json").toFile(), convertGravityCorrectionModelData(gravityCorrectionModelData));

        MagneticsCorrectionModelData magneticsCorrectionModelData = unmarshalData(basePath, MagneticsCorrectionModelData.class, "magneticsCorrectionModels.xml");
        objectMapper.writeValue(basePath.resolve("magneticsCorrectionModels.json").toFile(), convertMagneticsCorrectionModelData(magneticsCorrectionModelData));

        WaterColumnCalibrationStateData waterColumnCalibrationStateData = unmarshalData(basePath, WaterColumnCalibrationStateData.class, "waterColumnCalibrationStates.xml");
        objectMapper.writeValue(basePath.resolve("waterColumnCalibrationStates.json").toFile(), convertWaterColumnCalibrationsStateData(waterColumnCalibrationStateData));

        SinglebeamVerticalDatumData singlebeamVerticalDatumData = unmarshalData(basePath, SinglebeamVerticalDatumData.class, "singlebeamVerticalDatums.xml");
        objectMapper.writeValue(basePath.resolve("singlebeamVerticalDatums.json").toFile(), convertSinglebeamVerticalDatumData(singlebeamVerticalDatumData));

        NavigationDatumData navigationDatumData = unmarshalData(basePath, NavigationDatumData.class, "navigationDatums.xml");
        objectMapper.writeValue(basePath.resolve("navigationDatums.json").toFile(), convertNavigationDatumData(navigationDatumData));

    }

    private static <T> T unmarshalData(Path basePath, Class<T> cls, String fileName) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(cls);
        return cls.cast(context.createUnmarshaller()
                .unmarshal(new FileReader(basePath.resolve(fileName).toFile())));
    }

    private static edu.colorado.cires.cruisepack.data.InstrumentData convertInstrumentData(InstrumentData instrumentDataXml) {
        edu.colorado.cires.cruisepack.data.InstrumentData instrumentDataJson = new edu.colorado.cires.cruisepack.data.InstrumentData();
        instrumentDataJson.setDataVersion(instrumentDataXml.getDataVersion());
        InstrumentGroupList instrumentGroupList = instrumentDataXml.getInstrumentGroups();
        if (instrumentGroupList != null) {
            instrumentDataJson.setInstrumentGroups(instrumentGroupList.getInstrumentGroups().stream().map(XmlToJson::convertInstrumentGroup).collect(Collectors.toList()));
        }
        return instrumentDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.InstrumentGroup convertInstrumentGroup(InstrumentGroup instrumentGroupXml) {
        edu.colorado.cires.cruisepack.data.InstrumentGroup instrumentGroupJson = new edu.colorado.cires.cruisepack.data.InstrumentGroup();
        instrumentGroupJson.setDataType(instrumentGroupXml.getDataType());
        instrumentGroupJson.setShortType(instrumentGroupXml.getShortType());
        InstrumentList instrumentList = instrumentGroupXml.getInstruments();
        if (instrumentList != null) {
            instrumentGroupJson.setInstruments(instrumentList.getInstruments().stream().map(XmlToJson::convertInstrument).collect(Collectors.toList()));
        }
        AdditionalFieldList additionalFieldList = instrumentGroupXml.getAdditionalFields();
        if (additionalFieldList != null) {
            instrumentGroupJson.setAdditionalFields(additionalFieldList.getAdditionalFields().stream().map(XmlToJson::convertAdditionalFields).collect(Collectors.toList()));
        }
        return instrumentGroupJson;
    }

    private static edu.colorado.cires.cruisepack.data.Instrument convertInstrument(Instrument instrumentXml) {
        edu.colorado.cires.cruisepack.data.Instrument instrumentJson = new edu.colorado.cires.cruisepack.data.Instrument();
        instrumentJson.setUuid(instrumentXml.getUuid());
        instrumentJson.setName(instrumentXml.getName());
        instrumentJson.setShortName(instrumentXml.getShortName());
        FileExtensionList fileExtensionList = instrumentXml.getFileExtensions();
        if (fileExtensionList != null) {
            instrumentJson.setFileExtensions(fileExtensionList.getFileExtensions());
        }
        instrumentJson.setUse(instrumentXml.isUse());
        instrumentJson.setFlatten(instrumentXml.isFlatten());
        return instrumentJson;
    }

    private static edu.colorado.cires.cruisepack.data.FileExtension convertFileExtensionData(FileExtension fileExtensionXml) {
        edu.colorado.cires.cruisepack.data.FileExtension fileExtensionJson = new edu.colorado.cires.cruisepack.data.FileExtension();
        fileExtensionJson.setExtension(fileExtensionXml.getExtension());
        return fileExtensionJson;
    }

    private static edu.colorado.cires.cruisepack.data.AdditionalField convertAdditionalFields(AdditionalField instrumentXml) {
        edu.colorado.cires.cruisepack.data.AdditionalField instrumentJson = new edu.colorado.cires.cruisepack.data.AdditionalField();
        instrumentJson.setType(instrumentXml.getType());
        AdditionalFieldOptionList additionalFieldOptionList = instrumentXml.getAdditionalFieldOptions();
        if (additionalFieldOptionList != null) {
            instrumentJson.setAdditionalFieldOptions(additionalFieldOptionList.getAdditionalFieldOptions().stream().map(XmlToJson::convertAdditionalFieldOption).collect(Collectors.toList()));
        }
        return instrumentJson;
    }

    private static edu.colorado.cires.cruisepack.data.AdditionalFieldOption convertAdditionalFieldOption(AdditionalFieldOption instrumentXml) {
        edu.colorado.cires.cruisepack.data.AdditionalFieldOption instrumentJson = new edu.colorado.cires.cruisepack.data.AdditionalFieldOption();
        instrumentJson.setDisplayValue(instrumentXml.getDisplayValue());
        instrumentJson.setCode(instrumentXml.getCode());
        return instrumentJson;
    }

    private static edu.colorado.cires.cruisepack.data.PortData convertPortData(PortData portDataXml) {
        edu.colorado.cires.cruisepack.data.PortData portDataJson = new edu.colorado.cires.cruisepack.data.PortData();
        portDataJson.setDataVersion(portDataXml.getDataVersion());
        PortList portList = portDataXml.getPorts();
        if (portList != null) {
            portDataJson.setPorts(portList.getPorts().stream().map(XmlToJson::convertPort).collect(Collectors.toList()));
        }
        return portDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.Port convertPort(Port portXml) {
        edu.colorado.cires.cruisepack.data.Port portJson = new edu.colorado.cires.cruisepack.data.Port();
        portJson.setName(portXml.getName());
        portJson.setUuid(portXml.getUuid());
        portJson.setUse(portXml.isUse());
        return portJson;
    }

    private static edu.colorado.cires.cruisepack.data.PersonData convertPersonData(PersonData peopleDataXml) {
        edu.colorado.cires.cruisepack.data.PersonData peopleDataJson = new edu.colorado.cires.cruisepack.data.PersonData();
        peopleDataJson.setDataVersion(peopleDataXml.getDataVersion());
        PersonList peopleList = peopleDataXml.getPeople();
        if (peopleList != null) {
            peopleDataJson.setPeople(peopleList.getPersons().stream().map(XmlToJson::convertPerson).collect(Collectors.toList()));
        }
        return peopleDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.Person convertPerson(Person personXml) {
        edu.colorado.cires.cruisepack.data.Person personJson = new edu.colorado.cires.cruisepack.data.Person();
        personJson.setName(personXml.getName());
        personJson.setOrganization(personXml.getOrganization());
        personJson.setPosition(personXml.getPosition());
        personJson.setStreet(personXml.getStreet());
        personJson.setCity(personXml.getCity());
        personJson.setState(personXml.getState());
        personJson.setZip(personXml.getZip());
        personJson.setCountry(personXml.getCountry());
        personJson.setEmail(personXml.getEmail());
        personJson.setPhone(personXml.getPhone());
        personJson.setUuid(personXml.getUuid());
        personJson.setOrcid(personXml.getOrcid());
        personJson.setUse(personXml.isUse());
        return personJson;
    }

    private static edu.colorado.cires.cruisepack.data.OrganizationData convertOrganizationData(OrganizationData organizationDataXml) {
        edu.colorado.cires.cruisepack.data.OrganizationData organizationDataJson = new edu.colorado.cires.cruisepack.data.OrganizationData();
        organizationDataJson.setDataVersion(organizationDataXml.getDataVersion());
        OrganizationList organizationList = organizationDataXml.getOrganizations();
        if (organizationList != null) {
            organizationDataJson.setOrganizations(organizationList.getOrganizations().stream().map(XmlToJson::convertOrganization).collect(Collectors.toList()));
        }
        return organizationDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.Organization convertOrganization(Organization organizationXml) {
        edu.colorado.cires.cruisepack.data.Organization organizationJson = new edu.colorado.cires.cruisepack.data.Organization();
        organizationJson.setName(organizationXml.getName());
        organizationJson.setStreet(organizationXml.getStreet());
        organizationJson.setCity(organizationXml.getCity());
        organizationJson.setState(organizationXml.getState());
        organizationJson.setZip(organizationXml.getZip());
        organizationJson.setCountry(organizationXml.getCountry());
        organizationJson.setEmail(organizationXml.getEmail());
        organizationJson.setPhone(organizationXml.getPhone());
        organizationJson.setUuid(organizationXml.getUuid());
        organizationJson.setUse(organizationXml.isUse());
        return organizationJson;
    }

    private static edu.colorado.cires.cruisepack.data.SeaData convertSeaData(SeaData seaXml) {
        edu.colorado.cires.cruisepack.data.SeaData seaDataJson = new edu.colorado.cires.cruisepack.data.SeaData();
        seaDataJson.setDataVersion(seaXml.getDataVersion());
        SeaList seaList = seaXml.getSeas();
        if (seaList != null) {
            seaDataJson.setSeas(seaList.getSeas().stream().map(XmlToJson::convertSea).collect(Collectors.toList()));
        }
        return seaDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.Sea convertSea(Sea seaXml) {
        edu.colorado.cires.cruisepack.data.Sea seaJson = new edu.colorado.cires.cruisepack.data.Sea();
        seaJson.setUuid(seaXml.getUuid());
        seaJson.setName(seaXml.getName());
        seaJson.setUse(seaXml.isUse());
        return seaJson;
    }

    private static edu.colorado.cires.cruisepack.data.ShipData convertShipData(ShipData shipXml) {
        edu.colorado.cires.cruisepack.data.ShipData shipDataJson = new edu.colorado.cires.cruisepack.data.ShipData();
        shipDataJson.setDataVersion(shipXml.getDataVersion());
        ShipList shipList = shipXml.getShips();
        if (shipList != null) {
            shipDataJson.setShips(shipList.getShips().stream().map(XmlToJson::convertShip).collect(Collectors.toList()));
        }
        return shipDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.Ship convertShip(Ship shipXml) {
        edu.colorado.cires.cruisepack.data.Ship shipJson = new edu.colorado.cires.cruisepack.data.Ship();
        shipJson.setName(shipXml.getName());
        shipJson.setUuid(shipXml.getUuid());
        shipJson.setUse(shipXml.isUse());
        return shipJson;
    }

    private static edu.colorado.cires.cruisepack.data.GravityCorrectionModelData convertGravityCorrectionModelData(GravityCorrectionModelData gravityCorrectionModelXml) {
        edu.colorado.cires.cruisepack.data.GravityCorrectionModelData gravityCorrectionModelDataJson = new edu.colorado.cires.cruisepack.data.GravityCorrectionModelData();
        gravityCorrectionModelDataJson.setDataVersion(gravityCorrectionModelXml.getDataVersion());
        GravityCorrectionModelList gravityCorrectionModelList = gravityCorrectionModelXml.getGravityCorrectionModels();
        if (gravityCorrectionModelList != null) {
            gravityCorrectionModelDataJson.setGravityCorrectionModels(gravityCorrectionModelList.getGravityCorrectionModels().stream().map(XmlToJson::convertGravityCorrectionModel).collect(Collectors.toList()));
        }
        return gravityCorrectionModelDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.GravityCorrectionModel convertGravityCorrectionModel(GravityCorrectionModel gravityCorrectionModelXml) {
        edu.colorado.cires.cruisepack.data.GravityCorrectionModel gravityCorrectionModelJson = new edu.colorado.cires.cruisepack.data.GravityCorrectionModel();
        gravityCorrectionModelJson.setName(gravityCorrectionModelXml.getName());
        gravityCorrectionModelJson.setUuid(gravityCorrectionModelXml.getUuid());
        return gravityCorrectionModelJson;
    }

    private static edu.colorado.cires.cruisepack.data.MagneticsCorrectionModelData convertMagneticsCorrectionModelData(MagneticsCorrectionModelData magneticsCorrectionModelXml) {
        edu.colorado.cires.cruisepack.data.MagneticsCorrectionModelData magneticsCorrectionModelDataJson = new edu.colorado.cires.cruisepack.data.MagneticsCorrectionModelData();
        magneticsCorrectionModelDataJson.setDataVersion(magneticsCorrectionModelXml.getDataVersion());
        MagneticsCorrectionModelList magneticsCorrectionModelList = magneticsCorrectionModelXml.getMagneticsCorrectionModels();
        if (magneticsCorrectionModelList != null) {
            magneticsCorrectionModelDataJson.setMagneticsCorrectionModels(magneticsCorrectionModelList.getMagneticsCorrectionModels().stream().map(XmlToJson::convertMagneticsCorrectionModel).collect(Collectors.toList()));
        }
        return magneticsCorrectionModelDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.MagneticsCorrectionModel convertMagneticsCorrectionModel(MagneticsCorrectionModel magneticsCorrectionModelXml) {
        edu.colorado.cires.cruisepack.data.MagneticsCorrectionModel magneticsCorrectionModelJson = new edu.colorado.cires.cruisepack.data.MagneticsCorrectionModel();
        magneticsCorrectionModelJson.setName(magneticsCorrectionModelXml.getName());
        magneticsCorrectionModelJson.setUuid(magneticsCorrectionModelXml.getUuid());
        return magneticsCorrectionModelJson;
    }

    private static edu.colorado.cires.cruisepack.data.WaterColumnCalibrationStateData convertWaterColumnCalibrationsStateData(WaterColumnCalibrationStateData waterColumnCalibrationsStateXml) {
        edu.colorado.cires.cruisepack.data.WaterColumnCalibrationStateData waterColumnCalibrationStateDataJson =  new edu.colorado.cires.cruisepack.data.WaterColumnCalibrationStateData();
        waterColumnCalibrationStateDataJson.setDataVersion(waterColumnCalibrationsStateXml.getDataVersion());
        WaterColumnCalibrationStateList waterColumnCalibrationStateList = waterColumnCalibrationsStateXml.getWaterColumnCalibrationStates();
        if (waterColumnCalibrationStateList != null) {
            waterColumnCalibrationStateDataJson.setWaterColumnCalibrationStates(waterColumnCalibrationStateList.getWaterColumnCalibrationStates().stream().map(XmlToJson::convertWaterColumnCalibrationState).collect(Collectors.toList()));
        }
        return waterColumnCalibrationStateDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.WaterColumnCalibrationState convertWaterColumnCalibrationState(WaterColumnCalibrationState waterColumnCalibrationStateXml) {
        edu.colorado.cires.cruisepack.data.WaterColumnCalibrationState waterColumnCalibrationStateJson = new edu.colorado.cires.cruisepack.data.WaterColumnCalibrationState();
        waterColumnCalibrationStateJson.setName(waterColumnCalibrationStateXml.getName());
        waterColumnCalibrationStateJson.setUuid(waterColumnCalibrationStateXml.getUuid());
        return waterColumnCalibrationStateJson;
    }

    private static edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatumData convertSinglebeamVerticalDatumData(SinglebeamVerticalDatumData singlebeamVerticalDatumXml) {
        edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatumData singlebeamVerticalDatumDataJson = new edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatumData();
        singlebeamVerticalDatumDataJson.setDataVersion(singlebeamVerticalDatumXml.getDataVersion());
        SinglebeamVerticalDatumList singlebeamVerticalDatumList = singlebeamVerticalDatumXml.getSinglebeamVerticalDatums();
        if (singlebeamVerticalDatumList != null) {
            singlebeamVerticalDatumDataJson.setSinglebeamVerticalDatums(singlebeamVerticalDatumList.getSinglebeamVerticalData().stream().map(XmlToJson::convertSinglebeamVerticalDatum).collect(Collectors.toList()));
        }
        return singlebeamVerticalDatumDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatum convertSinglebeamVerticalDatum(SinglebeamVerticalDatum singlebeamVerticalDatumXml) {
        edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatum singlebeamVerticalDatumJson = new edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatum();
        singlebeamVerticalDatumJson.setName(singlebeamVerticalDatumXml.getName());
        singlebeamVerticalDatumJson.setUuid(singlebeamVerticalDatumXml.getUuid());
        return singlebeamVerticalDatumJson;
    }

    private static edu.colorado.cires.cruisepack.data.NavigationDatumData convertNavigationDatumData(NavigationDatumData navigationDatumXml) {
        edu.colorado.cires.cruisepack.data.NavigationDatumData navigationDatumDataJson = new edu.colorado.cires.cruisepack.data.NavigationDatumData();
        navigationDatumDataJson.setDataVersion(navigationDatumXml.getDataVersion());
        NavigationDatumList navigationDatumList = navigationDatumXml.getNavigationDatums();
        if (navigationDatumList != null) {
            navigationDatumDataJson.setNavigationDatums(navigationDatumList.getNavigationData().stream().map(XmlToJson::convertNavigationDatum).collect(Collectors.toList()));
        }
        return navigationDatumDataJson;
    }

    private static edu.colorado.cires.cruisepack.data.NavigationDatum convertNavigationDatum(NavigationDatum navigationDatumXml) {
        edu.colorado.cires.cruisepack.data.NavigationDatum navigationDatumJson = new edu.colorado.cires.cruisepack.data.NavigationDatum();
        navigationDatumJson.setName(navigationDatumXml.getName());
        navigationDatumJson.setUuid(navigationDatumXml.getUuid());
        return navigationDatumJson;
    }
}