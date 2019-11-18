# Lendic backend applicant test

The aim of the application is to create schedule repayment plan for given loan, taking into consideration the given nominal rate, and the duration of the payment (as months).

## Service Description
You should be able to start the example application by executing com.lendic.LendicoApplication, which starts a webserver on port 8080 (http://localhost:8080) and serves SwaggerUI where can inspect and try existing endpoints.

The project is based on a small web service which uses the following technologies:

* Java 1.8
* Spring Boot
* Maven

And for testing:
* Junit 5

SwaggerUI can be reach by URL:
http://localhost:8080
or
http://localhost:8080/swagger-ui.html

You should be aware of the following conventions while you are working on this exercise:

 * All new entities should have an ID with type of Long and a date_created with type of ZonedDateTime.
 * The architecture of the web service is built with the following components:
 	* DTOs: Objects which are used for outside communication via the API
    * Controller: Implements the processing logic of the web service, parsing of parameters and validation of in- and outputs.
    * Service: Implements the business logic and handles the access to the PaymentBean.
    * PaymentBean: Interface for the main POJO for payment data. (Can be used in future as DB interface in case the user want to save their plan).

To test the service we can using example as Request:

POST: localhost:8080/generate-plan
{
"loanAmount": "5000",
"nominalRate": "5",
"duration": "5",
"startDate": "2020-01-01T00:00:01Z"
}

values can be sent as string or numbers, but date should always be sent as string of pattern YYYY-MM-DDThh:mm:ssZ

And the expected Response:
[
    {
        "borrowerPaymentAmount": "1012.53",
        "date": "2020-02-01T00:00:01Z",
        "initialOutstandingPrincipal": "5000.0",
        "interest": "20.84",
        "principal": "991.69",
        "remainingOutstandingPrincipal": "4008.32"
    },
    {
        "borrowerPaymentAmount": "1012.53",
        "date": "2020-03-01T00:00:01Z",
        "initialOutstandingPrincipal": "4008.32",
        "interest": "16.71",
        "principal": "995.82",
        "remainingOutstandingPrincipal": "3012.51"
    },
    {
        "borrowerPaymentAmount": "1012.53",
        "date": "2020-04-01T00:00:01Z",
        "initialOutstandingPrincipal": "3012.51",
        "interest": "12.56",
        "principal": "999.97",
        "remainingOutstandingPrincipal": "2012.55"
    },
    {
        "borrowerPaymentAmount": "1012.53",
        "date": "2020-05-01T00:00:01Z",
        "initialOutstandingPrincipal": "2012.55",
        "interest": "8.39",
        "principal": "1004.14",
        "remainingOutstandingPrincipal": "1008.42"
    },
    {
        "borrowerPaymentAmount": "1012.63",
        "date": "2020-06-01T00:00:01Z",
        "initialOutstandingPrincipal": "1008.42",
        "interest": "4.21",
        "principal": "1008.42",
        "remainingOutstandingPrincipal": "0.0"
    }
]

The formula which had been used in the calcution is:
https://financeformulas.net/Annuity_Payment_Formula.html

Thanks!
ALAA TARE