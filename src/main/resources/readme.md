POST http://localhost:8080/fhir/Communication?_format=json&_pretty=true
Content-Type application/fhir+json; charset=UTF-8

{
  "resourceType": "Communication",
  "id": "1",
  "text": {
    "status": "generated",
    "div": "<div xmlns=\"http://www.w3.org/1999/xhtml\">Patient has very high serum potassium</div>"
  },
  "identifier": [
    {
      "type": {
        "text": "Paging System"
      },
      "system": "urn:oid:1.3.4.5.6.7",
      "value": "2345678901"
    }
  ],
  "definition": [
    {
      "display": "Hyperkalemia"
    }
  ],
  "partOf": [
    {
      "display": "Serum Potassium Observation"
    }
  ],
  "status": "completed",
  "category": [
    {
      "coding": [
        {
          "system": "http://acme.org/messagetypes",
          "code": "Alert"
        }
      ],
      "text": "Alert"
    }
  ],
  "medium": [
    {
      "coding": [
        {
          "system": "http://hl7.org/fhir/v3/ParticipationMode",
          "code": "WRITTEN",
          "display": "written"
        }
      ],
      "text": "written"
    }
  ],
  "subject": {
    "reference": "Patient/737623"
  },
  "recipient": [
    {
      "reference": "Practitioner/1400909"
    }
  ],
  "context": {
    "reference": "Encounter/1424405"
  },
  "sent": "2014-12-12T18:01:10-08:00",
  "received": "2014-12-12T18:01:11-08:00",
  "sender": {
    "reference": "Device/1252022"
  },
  "payload": [
    {
      "contentString": "Patient 1 has a very high serum potassium value (7.2 mmol/L on 2014-Dec-12 at 5:55 pm)"
    },
    {
      "contentReference": {
        "display": "Serum Potassium Observation"
      }
    }
  ]
}