package com.corpseed.serviceImpl;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.City;
import com.corpseed.entity.CmsPages;
import com.corpseed.entity.contactentity.Contact;
import com.corpseed.entity.contactentity.ContactAddress;
import com.corpseed.entity.Country;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.Review;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.State;
import com.corpseed.repository.regionrepo.CityRepository;
import com.corpseed.repository.CmsPagesRepository;
import com.corpseed.repository.contactrepo.ContactAddressRepository;
import com.corpseed.repository.contactrepo.ContactRepository;
import com.corpseed.repository.regionrepo.CountryRepository;
import com.corpseed.repository.enqrepo.EnquiryRepository;
import com.corpseed.repository.ReviewRepository;
import com.corpseed.repository.regionrepo.StateRepository;

@Service
public class MasterService {
	
	@Autowired
	private CmsPagesRepository  cmsPagesRepository;
	
	@Autowired
	private ContactAddressRepository  contactAddressRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private CityRepository cityRepository;
		
	@Autowired
	private EnquiryRepository enquiryRepository;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
 

	@CacheEvict(value = "footerCat",allEntries = true)
	public CmsPages saveCmsPages(@Valid CmsPages cmsPages) {
		return this.cmsPagesRepository.save(cmsPages);
	}
	
	public List<CmsPages> getAllCmsPages() {
		return this.cmsPagesRepository.findByDeleteStatusOrderByIdDesc(2);
	}
	@CacheEvict(value = "address",allEntries = true)	
	public ContactAddress saveAddress(@Valid ContactAddress address) {
		return this.contactAddressRepository.save(address);
	}

	public List<ContactAddress> getAllContactAddress() {
		return this.contactAddressRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Country saveCountry(@Valid Country country) {
		return this.countryRepository.save(country);
	}

	public List<Country> getAllCountry() {
		return this.countryRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Country findByCountryCodeAndShortName(String countryCode, String shortName) {
		return this.countryRepository.findByCountryCodeAndShortName(countryCode,shortName);
	}

	public Country getCountryById(String countryId) {
		return this.countryRepository.getOne(Long.parseLong(countryId));
	}

	public State findByStateNameAndCountry(String stateName, Country country) {
		return this.stateRepository.findByStateNameAndCountryAndDeleteStatus(stateName,country,2);
	}

	public State saveState(@Valid State state) {
		return this.stateRepository.save(state);
	}

	public List<State> getAllStates() {
		return this.stateRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public List<State> getAllStatesByCountryId(Country country) {
		return this.stateRepository.findByCountryAndDeleteStatus(country,2);
	}

	public State getStateById(String stateId) {
		return this.stateRepository.getOne(Long.parseLong(stateId));
	}

	public City findByCityNameAndState(String cityName, State state) {
		return this.cityRepository.findByCityNameAndStateAndDeleteStatus(cityName, state,2);
	}
	@CacheEvict(value = "findByCityNameLike",allEntries = true)
	public City saveCity(@Valid City city) {
		return this.cityRepository.save(city);
	}

	public List<City> getAllCity() {
		return this.cityRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public CmsPages getCmsPageByUuid(String pageuuid) {
		return this.cmsPagesRepository.findByUuidAndDeleteStatus(pageuuid,2);
	}

	public ContactAddress getContactAddressByUuid(String addressuuid) {
		return this.contactAddressRepository.findByUuidAndDeleteStatus(addressuuid,2);
	}

	public Country getCountryByUuid(String countryuuid) {
		return this.countryRepository.findByUuidAndDeleteStatus(countryuuid,2);
	}

	public Country findByCountryCodeAndShortNameAndUuidNot(String countryCode, String shortName, String countryuuid) {
		return this.countryRepository.findByCountryCodeAndShortNameAndUuidNot(countryCode,shortName,countryuuid);
	}

	public State getStateByUuid(String stateuuid) {
		return this.stateRepository.findByUuidAndDeleteStatus(stateuuid,2);
	}

	public State findByStateNameAndCountryAndUuidNot(String stateName, Country country, String stateuuid) {
		return this.stateRepository.findByStateNameAndCountryAndUuidNot(stateName, country,stateuuid);
	}

	public City getCityByUuid(String cityuuid) {
		return this.cityRepository.findByUuidAndDeleteStatus(cityuuid,2);
	}

	public City findByCityNameAndStateAndUuidNot(String cityName, State state, String cityuuid) {
		return this.cityRepository.findByCityNameAndStateAndDeleteStatusAndUuidNot(cityName, state,2, cityuuid);
	}
	@CacheEvict(value = "footerCat",allEntries = true)
	public void deleteCmsPages(long id) {
		this.cmsPagesRepository.deleteById(id);
	}
	
	@CacheEvict(value = "address",allEntries = true)	
	public void deleteContactAddress(long id) {
		this.contactAddressRepository.deleteById(id);
	}

	public void deleteCountry(long id) {
		this.countryRepository.deleteById(id);
	}

	public void deleteState(Long id) {
		this.stateRepository.deleteById(id);
	}

	@CacheEvict(value = "findByCityNameLike",allEntries = true)
	public void deleteCity(long id) {
		this.cityRepository.deleteById(id);
	}

	@Cacheable("footerCat")
	public List<CmsPages> findByDisplayStatus(String status) {
		return this.cmsPagesRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public List<Enquiry> getAllEnquiry() {
		return this.enquiryRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Enquiry getEnquiryByUuid(String uuid) {
		return this.enquiryRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	public void deleteEnquiry(long id) {
		this.enquiryRepository.deleteById(id);
	}

	public List<Services> getAllServices() {
		return this.servicesService.getAllServices();
	}

	public Enquiry saveEnquiry(@Valid Enquiry enquiry) {
		return this.enquiryRepository.save(enquiry);
	}

	@Cacheable("findByCityNameLike")
	public List<City> findByCityNameContaining(String value) {
		return this.cityRepository.findByCityNameContainingAndDeleteStatus(value,2);
	}

	public List<CmsPages> getRecentSixCmsPages() {
		return this.cmsPagesRepository.findTop6ByDeleteStatusOrderByIdDesc(2);
	}

	public List<Contact> getAllContact() {
		return this.contactRepository.findByDeleteStatusOrderByIdDesc(2);
	}
	
	@Cacheable("contact")
	public Contact getContactByStatus(String status) {
		return this.contactRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	@CacheEvict(value = "contact",allEntries = true)	
	public Contact saveContact(@Valid Contact contact) {
		return this.contactRepository.save(contact);
	}

	public Contact findByMobileOrEmail(String mobile, String email) {
		return this.contactRepository.findByMobileOrEmail(mobile,email);
	}

	public Contact getContactByUuid(String contactuuid) {
		return this.contactRepository.findByUuidAndDeleteStatus(contactuuid,2);
	}
	@CacheEvict(value = "contact",allEntries = true)	
	public void deleteContact(long id) {
		this.contactRepository.deleteById(id);
	}

	public Contact getContactByStatusAndUuidNot(String status, String uuid) {
		return this.contactRepository.findByDisplayStatusAndDeleteStatusAndUuidNot(status,2,uuid);
	}

	public Contact findByMobileAndUuidNot(String mobile, String uuid) {
		return this.contactRepository.findByMobileAndDeleteStatusAndUuidNot(mobile,2, uuid);
	}

	public Contact findByEmailAndUuidNot(String email, String uuid) {
		return this.contactRepository.findByEmailAndDeleteStatusAndUuidNot(email,2, uuid);
	}

	public List<Review> getAllReviews() {
		return this.reviewRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@CacheEvict(value = "reviews",allEntries = true)
	public Review saveReview(@Valid Review review) {
		return this.reviewRepository.save(review);
	}

	public Review findByUuid(String uuid) {
		return this.reviewRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "reviews",allEntries = true)
	public void deleteReview(long id) {
		this.reviewRepository.deleteById(id);
	}

	@Cacheable(value = "reviews")
	public List<Review> findByShowHomeStatusAndDisplayStatus(String status1, String status2) {
		return this.reviewRepository.findByShowHomeStatusAndDisplayStatusAndDeleteStatus(status1, status2,2);
	}

	@Cacheable("address")
	public List<ContactAddress> findContactByStatus(String status) {
		return this.contactAddressRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public CmsPages findBySlug(String slug) {
		return this.cmsPagesRepository.findBySlugAndDeleteStatus(slug,2);
	}

	public CmsPages findBySlugAndUuidNot(String slug, String uuid) {
		return this.cmsPagesRepository.findBySlugAndUuidNot(slug,uuid);
	}

	public void saveStates(List<State> state) {
		this.stateRepository.saveAll(state);
	}

	@CacheEvict(value = "findByCityNameLike",allEntries = true)
	public void saveCities(List<City> city) {
		this.cityRepository.saveAll(city);
	}

	public CmsPages findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.cmsPagesRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public ContactAddress findAddressByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.contactAddressRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public Country findCountryByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.countryRepository.findByIdAndDeleteStatus(typeId, dStatus);
	}

	public State findStateByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.stateRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public City findCityByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.cityRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public Contact findContactByIdAndStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.contactRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public Review findReviewByIdAndStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.reviewRepository.findByIdAndDeleteStatus(typeId,dStatus);		
	}

	public Page<Enquiry> getEnquiry(Pageable pageable) {
		// TODO Auto-generated method stub
		return this.enquiryRepository.findByDisplayStatusAndDeleteStatus("1",2,pageable);
	}

	public Page<Enquiry> getEnquiry(String fromDate, String toDate, Pageable pageable) {
		return this.enquiryRepository.findByDisplayStatusAndDeleteStatusAndPostDateBetween("1",2,fromDate,toDate,pageable);
	}

	public State findStateById(Long stateId) {
		return this.stateRepository.findById(stateId).orElse(null);
	}

	public List<City> findAllCityByStateId(State state) {
		return this.cityRepository.findByState(state);
	}

	public City findByCityName(String cityName) {
		return this.cityRepository.findByCityName(cityName);
	}
}
