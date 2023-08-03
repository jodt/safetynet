# SafetyNet Alerts Project

The purpose of this application is to send information to emergency services systems.
For example, in the event of a flood, we want to provide emergency services with accurate information about people in
the area. We need to know the potential victims, their age and their medical history (treatments, allergies, etc.).
Here, I created the API to retrieve this information through different endpoints.

## Getting Started

First clone this project, open it in your IDE and run the queries to get the information

### Prerequisites

IDE (Intellij, Eclipse...)  
JAVA  
MAVEN

### Endpoints

GET

* http://localhost:8080/person/all
    * for retrieve all people

POST / PUT / DELETE

* http://localhost:8080/person
    * for add, update or delete person

GET

* http://localhost:8080/firestation/all
    * for retrieve all fire stations

POST / PUT / DELETE

* http://localhost:8080/firestation
    * for add, update or delete a fire station

GET

* http://localhost:8080/medicalRecord/all
    * for retrieve all medical records

POST / PUT / DELETE

* http://localhost:8080/medicalRecord
    * for add, update or delete a medical record

GET

* http://localhost:8080/firestation?stationNumber=<station_number>
    * This url returns a list of people covered by the corresponding fire station. The list includes the following
      specific information: first name, last name, address, telephone number. Moreover,
      it provides a count of the number of adults and the number of children (any individual aged 18 or
      less) in the service area.

GET

* http://localhost:8080/childAlert?address=<address>
    * This url returns a list of children (any individual aged 18 or under) living at this address.
      The list includes each child's first and last name, age, and a list of other
      household members.

GET

* http://localhost:8080/phoneAlert?firestation=<firestation_number>
    * This url returns a list of the telephone numbers of the residents served by the fire station.

GET

* http://localhost:8080/fire?address=<address>
    * This url returns the list of inhabitants living at the given address as well as the number of the fire station
      servant. The list includes name, phone number, age and background medical conditions (drugs, dosage and allergies)
      of each person.

GET

* http://localhost:8080/flood/stations?stations=<a list of station_numbers>
    * This url returns the list of inhabitants living at the given address as well as the number of the fire station
      servant. The list includes name, phone number, age and background medical conditions (drugs, dosage and allergies)
      of each person.

GET

* http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
    * This url returns the name, address, age, email address and medical history (medications, dosage, allergies) of
      each inhabitant.

GET

* http://localhost:8080/communityEmail?city=<city>
    * This url returns the email addresses of all the inhabitants of the city.

## Authors

* Joel DUMORTIER

