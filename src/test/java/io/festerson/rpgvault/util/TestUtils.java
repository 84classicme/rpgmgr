package io.festerson.rpgvault.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.generated.GetCountryRequest;
import com.generated.GetCountryResponse;
import com.sun.security.auth.UserPrincipal;
import io.festerson.rpgvault.campaigns.CampaignRepository;
import io.festerson.rpgvault.campaigns.CampaignRequest;
import io.festerson.rpgvault.characters.CharacterRepository;
import io.festerson.rpgvault.domain.*;
import io.festerson.rpgvault.players.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import reactor.core.publisher.Flux;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestUtils {

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    PlayerRepository playerRepository;

    public static final String PLAYER_ID = "TEST_ID";
    public static final String PLAYER_NAME = "TEST_PLAYER";
    public static final String CAMPAIGN_NAME = "TEST_CAMPAIGN";
    public static final String CHARACTER_NAME = "TEST_CHARACTER";
    public static final List<String> PLAYER_IDS = Arrays.asList(PLAYER_ID, "5e078469fc8fb856ec68fd99", "5e078469fc8fb856ec68fd99");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final List<String> FIRST_NAMES = Arrays.asList("Ragnor", "Dracca", "Corellion", "Thunk", "Garl", "Brevnor", "Vera");
    public static final List<String> LAST_NAMES = Arrays.asList("Stonebane", "Mannix", "Brightstar", "Goreman", "Lightfingers", "Pox", "Krinkles");


    public static Principal buildSecurityPrincipal(){
        UserPrincipal userPrincipal = new UserPrincipal("TEST");
        return  userPrincipal;
    }

    public static String generateName(){
        Collections.shuffle(FIRST_NAMES);
        Collections.shuffle(LAST_NAMES);
        return FIRST_NAMES.get(0) + " " + LAST_NAMES.get(0);
    }

    public static int generateAbility(){
        Random RND = new Random();
        return RND.nextInt((18 - 8) + 1) + 8;
    }

    public static int generateAc(){
        Random RND = new Random();
        return RND.nextInt((18 - 11) + 1) + 11;
    }

    public static int generateHp(){
        Random RND = new Random();
        return RND.nextInt((14 - 8) + 1) + 8;
    }

    public static Campaign buildCampaign() throws Exception{
       return Campaign
            .builder()
            .name("test_campaign")
            .startDate(DATE_FORMAT.parse("2000-1-11"))
            .endDate(DATE_FORMAT.parse("2100-2-22"))
            .playerIds(Arrays.asList("p10", "p11"))
            .characterIds(Arrays.asList("c12", "c13"))
            .npcIds(Arrays.asList("n14", "n15"))
            .monsterIds(Arrays.asList("m16", "m17"))
            .dmId("ptest")
            .description("description")
            .imageUrl("imgurl")
            .build();
    }

    public static PlayerCharacter buildCharacter(){
        return PlayerCharacter
            .builder()
            .name("test_character")
            .crace(CharacterRace.pickOne())
            .cclass(CharacterClass.pickOne())
            .level(1)
            .strength(TestUtils.generateAbility())
            .dexterity(TestUtils.generateAbility())
            .constitution(TestUtils.generateAbility())
            .wisdom(TestUtils.generateAbility())
            .intelligence(TestUtils.generateAbility())
            .charisma(TestUtils.generateAbility())
            .ctype(CharacterType.PC)
            .ac(generateAc())
            .hp(generateHp())
            .imageUrl("http://example.com/img1")
            .playerId("imgurl")
            .build();
    }

    public static Player buildPlayer(){
        return buildPlayer(null,"test", "player", "me@notu.com", "US", "http://www.imgurl");
    }

    public static Player buildPlayer(String id, String firstName, String lastName, String email, String country, String imageUrl){
        return Player
            .builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .country(country)
            .imageUrl(imageUrl)
            .build();
    }

    public static Flux<Campaign> buildCampaignRepositoryTestCollection() throws Exception {
        Campaign campaign1 = buildCampaign();
        Campaign campaign2 = buildCampaign();
        Campaign campaign3 = buildCampaign();
        campaign1.setName(CAMPAIGN_NAME);
        campaign2.setPlayerIds(PLAYER_IDS);
        campaign3.setPlayerIds(PLAYER_IDS);
        return (Flux.just(campaign1, campaign2, campaign3, buildCampaign(), buildCampaign()));
    }

    public static Flux<PlayerCharacter> buildCharacterRepositoryTestCollection(){
        PlayerCharacter playerCharacter1 = buildCharacter();
        PlayerCharacter playerCharacter2 = buildCharacter();
        PlayerCharacter playerCharacter3 = buildCharacter();
        playerCharacter1.setPlayerId(PLAYER_ID);
        playerCharacter2.setPlayerId(PLAYER_ID);
        playerCharacter3.setName(CHARACTER_NAME);
        return(Flux.just(playerCharacter1, playerCharacter2, playerCharacter3, buildCharacter(), buildCharacter()));
    }

    public static Flux<Player> buildPlayers(){
        return(Flux.just(buildPlayer("10001", "Annie", "Jones", "aj@example.com", "US","http://example.com/aj.jpg"),
            buildPlayer("10002","John","Johnson", "jj@example.com", "US","http://example.com/jj.jpg"),
            buildPlayer("10003","David", "Davidson", "dd@example.com", "US","http://example.com/dd.jpg")));
    }

    public static Flux<Player> buildPlayersToUpdate(){
        return(Flux.just(buildPlayer("10010", "Pat", "Samples", "ps@example.com", "US","http://example.com/ps.jpg"),
            buildPlayer("10011","Ben","Hur", "bh@example.com", "US","http://example.com/bh.jpg"),
            buildPlayer("10012","Peaches", "Moscowitz", "pm@example.com", "US","http://example.com/pm.jpg"),
            buildPlayer("10013","Lisa", "Simpson", "ls@example.com", "US","http://example.com/ls.jpg"),
            buildPlayer("10014","Adam", "West", "aw@example.com", "US","http://example.com/aw.jpg")));
    }

    public static Flux<Player> buildPlayersToDelete(){
        return(Flux.just(buildPlayer("10020", "Johnny", "Guitar", "jg@example.com", "US","http://example.com/jg.jpg"),
            buildPlayer("10021","Sam","Spade", "ss@example.com", "US","http://example.com/s.jpg"),
            buildPlayer("10022","Petulia", "Frizzlefoam", "pf@example.com", "US","http://example.com/pf.jpg")));
    }

    public static CampaignRequest getCampaignRequestFromJson(String path) throws IOException {
        if (path == null || path.isEmpty()) path = "src/test/resources/json/campaign.json";
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, CampaignRequest.class);
    }

    public static GetCountryRequest getCountryRequestFromXml(String path) throws IOException {
        if (path == null || path.isEmpty()) path = "src/test/resources/xml/CampaignRequest.xml";
        File file = new File(path);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = inputStreamToString(new FileInputStream(file));
        return xmlMapper.readValue(xml, GetCountryRequest.class);
    }

    public static GetCountryResponse getCountryResponseFromXml(String path) throws IOException {
        if (path == null || path.isEmpty()) path = "src/test/resources/xml/CountryResponse.xml";
        File file = new File(path);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = inputStreamToString(new FileInputStream(file));
        return xmlMapper.readValue(xml, GetCountryResponse.class);
    }

    private static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static <T> String serializeObject(T object, String nsURI, String nsPrefix){
        if (nsURI == null || nsURI.isBlank()) nsURI = "http://example.com/endpoint";
        if (nsPrefix == null || nsPrefix.isBlank()) nsPrefix = "tns1";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Class clazz = object.getClass();
        String responseRootTag = StringUtils.uncapitalize(clazz.getSimpleName());
        QName payloadName = new QName(nsURI,responseRootTag,nsPrefix);
        try{
            JAXBContext ctx = JAXBContext.newInstance(clazz);
            Marshaller objectMarshaller = ctx.createMarshaller();

            JAXBElement<T> jaxbElement = new JAXBElement<>(payloadName, clazz, null, object);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            objectMarshaller.marshal(jaxbElement, document);

            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPBody body = soapMessage.getSOAPPart().getEnvelope().getBody();
            body.addDocument(document);

            //byteArrayOutputStream = new ByteArrayOutputStream();
            soapMessage.saveChanges();
            soapMessage.writeTo(byteArrayOutputStream);

        } catch (JAXBException | ParserConfigurationException | SOAPException | IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    public static <T> T deserializeSoapRequest(String soapRequest, Class<T> clazz){
        XMLInputFactory xif = XMLInputFactory.newFactory();
        JAXBElement<T> jb = null;
        try{
            XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(soapRequest));
            do{
                xsr.nextTag();
            } while(!xsr.getLocalName().equals("Body"));
            xsr.nextTag();
            JAXBContext ctx = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            jb = unmarshaller.unmarshal(xsr, clazz);
            xsr.close();
        } catch (XMLStreamException | JAXBException e) {
            e.printStackTrace();
        }
        return jb.getValue();
    }

    private static XPath getXPathFactory(){
        Map<String, String> namespaceUris = new HashMap<>();
        namespaceUris.put("xml", XMLConstants.XML_NS_URI);
        namespaceUris.put("soap", "http://schemas.xmlsoap.org/soap/envelope");
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if(namespaceUris.containsKey(prefix)) {
                    return namespaceUris.get(prefix);
                }else {
                    return XMLConstants.NULL_NS_URI;
                }
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });
        return xPath;
    }
}
