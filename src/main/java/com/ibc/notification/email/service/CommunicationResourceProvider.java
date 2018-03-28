package com.ibc.notification.email.service;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.utils.ResourceUtilities;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Communication;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;


/**
 * This is a resource provider which stores Patient resources in memory using a HashMap. This is obviously not a production-ready solution for many reasons, 
 * but it is useful to help illustrate how to build a fully-functional server.
 */
public class CommunicationResourceProvider implements IResourceProvider {

	/**
	 * This map has a resource ID as a key, and each key maps to a Deque list containing all versions of the resource with that ID.
	 */
	private Map<Long, Deque<Communication>> myIdToPatientVersions = new HashMap<Long, Deque<Communication>>();

	/**
	 * This is used to generate new IDs
	 */
	private long myNextId = 1;

	/**
	 * Constructor, which pre-populates the provider with one resource instance.
	 */
	public CommunicationResourceProvider() {
		long resourceId = myNextId++;
		
		Communication communication = new Communication();
		communication.setId(Long.toString(resourceId));
		communication.addIdentifier();
		communication.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
		communication.getIdentifier().get(0).setValue("00002");
		//communication.addName().addFamily("Test");
		//communication.getName().get(0).addGiven("PatientOne");
		//communication.setGender(AdministrativeGenderEnum.FEMALE);

		LinkedList<Communication> list = new LinkedList<Communication>();
		list.add(communication);
		
		myIdToPatientVersions.put(resourceId, list);

	}

	/**
	 * Stores a new version of the patient in memory so that it can be retrieved later.
	 * 
	 * @param thePatient
	 *            The patient resource to store
	 * @param theId
	 *            The ID of the patient to retrieve
	 */
	private void addNewVersion(Communication theCommunication, Long theId) {
		InstantDt publishedDate;
		if (!myIdToPatientVersions.containsKey(theId)) {
			myIdToPatientVersions.put(theId, new LinkedList<Communication>());
			publishedDate = InstantDt.withCurrentTime();
		} else {
			Communication currentCommunication = myIdToPatientVersions.get(theId).getLast();
			Map<ResourceMetadataKeyEnum<?>, Object> resourceMetadata = currentCommunication.getResourceMetadata();
			publishedDate = (InstantDt) resourceMetadata.get(ResourceMetadataKeyEnum.PUBLISHED);
		}

		/*
		 * PUBLISHED time will always be set to the time that the first version was stored. UPDATED time is set to the time that the new version was stored.
		 */
		theCommunication.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, publishedDate);
		theCommunication.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, InstantDt.withCurrentTime());

		Deque<Communication> existingVersions = myIdToPatientVersions.get(theId);

		// We just use the current number of versions as the next version number
		String newVersion = Integer.toString(existingVersions.size());
		
		// Create an ID with the new version and assign it back to the resource
		IdDt newId = new IdDt("Patient", Long.toString(theId), newVersion);
		theCommunication.setId(newId);
		
		existingVersions.add(theCommunication);
	}

	/**
	 * The "@Create" annotation indicates that this method implements "create=type", which adds a 
	 * new instance of a resource to the server.
	 */
	@Create()
	public MethodOutcome createCommunication(@ResourceParam Communication theCommunication) {
		validateResource(theCommunication);

		//THe code below all comes from Patient
		// Here we are just generating IDs sequentially
		//long id = myNextId++;

		//addNewVersion(theCommunication, id);

		// Let the caller know the ID of the newly created resource
		//MethodOutcome m = new MethodOutcome(new IdDt(2L));
		//m.setResource(theCommunication);
		return new MethodOutcome(new IdDt(2L)).setResource(theCommunication);
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must be overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Communication> getResourceType() {
		return Communication.class;
	}

	/**
	 * This method just provides simple business validation for resources we are storing.
	 * 
	 * @param theCommunication
	 *            The patient to validate
	 */
	private void validateResource(Communication theCommunication) {
		/*
		 * Our server will have a rule that patients must have a family name or we will reject them
		 */
		/*if (theCommunication.getNameFirstRep().getFamilyFirstRep().isEmpty()) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails("No family name provided, Patient resources must have at least one family name.");
			throw new UnprocessableEntityException(outcome);
			
		}*/
	}
	
	public static void main (String[] args) {
		MethodOutcome m = new CommunicationResourceProvider().createCommunication(new Communication());
	}

}
