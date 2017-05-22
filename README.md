### Tacyt JAVA SDK ###


#### PREREQUISITES ####

* Java 1.5 or above.

* Read API documentation (https://tacyt.elevenpaths.com/help/api).

* To get the "API ID" and "Secret" (fundamental values for integrating Tacyt in any application) it’s necessary to have an account in Tacyt's website.


#### USING THE SDK IN JAVA ####

* Include all SDK files and dependencies in your project.

* Create a Tacyt object with the "API ID" and "Secret" previously obtained.
```
     Tacyt tacyt = new Tacyt(API_ID, SECRET);
```

* Call to Tacyt Server to do searches, define tags, create filters or get the RSS info ...
```
     TacytResponse tacytResponse = tacyt.searchApps("packageName:com.whatsapp", 0, 10);

     TacytResponse tacytResponse = tacyt.assignTag("test", Arrays.asList(new String[]{"com.linterna11GooglePlay", "com.sms.kat1aptoideapps"}));

     List<Filter.Rule> rules = new ArrayList<Filter.Rule>();
     rules.add(new Filter.Rule(1, "contains(description,\"whatsapp\")"));
     Filter filter = new Filter("Whatsapp clones", "Apps which has the word Whatsapp in its description", 1, Filter.Visibility.PUBLIC, rules);
     TacytResponse tacytResponse = tacyt.createFilter(filter);

     TacytResponse tacytResponse = tacyt.getRSSinfo(filterId);
```

* After every API call, get Tacyt response data and errors and handle them.
```
     JsonObject jObject = tacytResponse.getData();
     com.elevenpaths.tacyt.Error error = tacytResponse.getError();
```


#### TROUBLESHOOTING ####

*A javax.net.ssl.SSLHandshakeException with a nested sun.security.validator.ValidatorException is thrown when invoking an API call.*

This exception is normally thrown when the JDK doesn't trust the CA that signs the digital certificate used in Tacyt's website (https://tacyt.elevenpaths.com). You may need to install the CA (http://www.startssl.com/certs/ca.pem) as a trusted certificate in your JDK's truststore (normally in jre/lib/security/cacerts) using the keytool utility.
