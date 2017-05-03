Grade Builds

* to start:
change application properties from
spring.jpa.hibernate.ddl-auto=update
to
spring.jpa.hibernate.ddl-auto=create


  ./gradlew bootRun

Configuration in Intellij IDEA:
* Ticking the "Enable annotation processing" checkbox in Settings->Compiler->Annotation Processors.

* Install the plugin of Lombok for idea and restart for change to take effect.