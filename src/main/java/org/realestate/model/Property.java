package org.realestate.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonPropertyOrder(
        {
                "comments",
                "url",
                "address",
                "zipCode",
                "propertyCost",
                "forSale",
                "leaseEndsOn",
                "lotSize",
                "cashOnCash",
                "initialInvestment",
                "year",
                "downPayment",
                "annualizedReturnIn5Years",
                "schoolRatings",
                "roofstockRatingStars",
                "nicheRating",
                "nicheAreaFeel",
                "nicheRentPercent",
                "nicheOwnPercent",
                "nicheMedianRent",
                "nicheMedianHomeValue",
                "nichePopulation",
                "zillowEstimatedValue",
                "zillowRentEstimatePerMonth",
                "zillowOwnerDescription",
                "zillowMedianNeighborHoodHomeValue",
                "zillowLastSoldFor",
                "zillowLastSoldOn",
        }
)
public class Property {
    private String comments;
    private String url;
    private String address;
    private String zipCode;
    private String propertyCost;
    private String forSale;
    private String leaseEndsOn;
    private String lotSize;
    private String cashOnCash;
    private String initialInvestment;
    private String year;
    private String downPayment;
    private String annualizedReturnIn5Years;
    private String schoolRatings;
    private String roofstockRatingStars;
    private String nicheRating;
    private String nicheAreaFeel;
    private String nicheRentPercent;
    private String nicheOwnPercent;
    private String nicheMedianRent;
    private String nicheMedianHomeValue;
    private String nichePopulation;
    private String zillowEstimatedValue;
    private String zillowRentEstimatePerMonth;
    private String zillowOwnerDescription;
    private String zillowMedianNeighborHoodHomeValue;
    private String zillowLastSoldFor;
    private String zillowLastSoldOn;


    public Property() {
    }

    public String getSchoolRatings() {
        return schoolRatings;
    }

    public void setSchoolRatings(String schoolRatings) {
        this.schoolRatings = schoolRatings;
    }

    public String getRoofstockRatingStars() {
        return roofstockRatingStars;
    }

    public void setRoofstockRatingStars(String roofstockRatingStars) {
        this.roofstockRatingStars = roofstockRatingStars;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getForSale() {
        return forSale;
    }

    public void setForSale(String forSale) {
        this.forSale = forSale;
    }

    public String getPropertyCost() {
        return propertyCost;
    }

    public void setPropertyCost(String propertyCost) {
        this.propertyCost = propertyCost;
    }

    @Override
    public String toString() {
        return "Property{" +
                "url='" + url + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", propertyCost='" + propertyCost + '\'' +
                ", forSale='" + forSale + '\'' +
                ", leaseEndsOn='" + leaseEndsOn + '\'' +
                ", lotSize='" + lotSize + '\'' +
                ", cashOnCash='" + cashOnCash + '\'' +
                ", initialInvestment='" + initialInvestment + '\'' +
                ", year='" + year + '\'' +
                ", downPayment='" + downPayment + '\'' +
                ", annualizedReturnIn5Years='" + annualizedReturnIn5Years + '\'' +
                ", nicheRating='" + nicheRating + '\'' +
                ", nicheAreaFeel='" + nicheAreaFeel + '\'' +
                ", nicheRentPercent='" + nicheRentPercent + '\'' +
                ", nicheOwnPercent='" + nicheOwnPercent + '\'' +
                ", nicheMedianRent='" + nicheMedianRent + '\'' +
                ", nicheMedianHomeValue='" + nicheMedianHomeValue + '\'' +
                ", nichePopulation='" + nichePopulation + '\'' +
                ", zillowEstimatedValue='" + zillowEstimatedValue + '\'' +
                ", zillowRentEstimatePerMonth='" + zillowRentEstimatePerMonth + '\'' +
                ", zillowOwnerDescription='" + zillowOwnerDescription + '\'' +
                ", zillowMedianNeighborHoodHomeValue='" + zillowMedianNeighborHoodHomeValue + '\'' +
                ", zillowLastSoldFor='" + zillowLastSoldFor + '\'' +
                ", zillowLastSoldOn='" + zillowLastSoldOn + '\'' +
                '}';
    }

    public String getLeaseEndsOn() {
        return leaseEndsOn;
    }

    public void setLeaseEndsOn(String leaseEndsOn) {
        this.leaseEndsOn = leaseEndsOn;
    }

    public String getLotSize() {
        return lotSize;
    }

    public void setLotSize(String lotSize) {
        this.lotSize = lotSize;
    }

    public String getZillowLastSoldFor() {
        return zillowLastSoldFor;
    }

    public void setZillowLastSoldFor(String zillowLastSoldFor) {
        this.zillowLastSoldFor = zillowLastSoldFor;
    }

    public String getZillowLastSoldOn() {
        return zillowLastSoldOn;
    }

    public void setZillowLastSoldOn(String zillowLastSoldOn) {
        this.zillowLastSoldOn = zillowLastSoldOn;
    }

    public String getZillowMedianNeighborHoodHomeValue() {
        return zillowMedianNeighborHoodHomeValue;
    }

    public void setZillowMedianNeighborHoodHomeValue(String zillowMedianNeighborHoodHomeValue) {
        this.zillowMedianNeighborHoodHomeValue = zillowMedianNeighborHoodHomeValue;
    }

    public String getZillowOwnerDescription() {
        return zillowOwnerDescription;
    }

    public void setZillowOwnerDescription(String zillowOwnerDescription) {
        this.zillowOwnerDescription = zillowOwnerDescription;
    }

    public String getZillowRentEstimatePerMonth() {
        return zillowRentEstimatePerMonth;
    }

    public void setZillowRentEstimatePerMonth(String zillowRentEstimatePerMonth) {
        this.zillowRentEstimatePerMonth = zillowRentEstimatePerMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Property)) return false;

        Property property = (Property) o;

        return new EqualsBuilder()
                .append(url, property.url)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .toHashCode();
    }

    public String getZillowEstimatedValue() {
        return zillowEstimatedValue;
    }

    public void setZillowEstimatedValue(String zillowEstimatedValue) {
        this.zillowEstimatedValue = zillowEstimatedValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCashOnCash() {
        return cashOnCash;
    }

    public void setCashOnCash(String cashOnCash) {
        this.cashOnCash = cashOnCash;
    }

    public String getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(String initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getAnnualizedReturnIn5Years() {
        return annualizedReturnIn5Years;
    }

    public void setAnnualizedReturnIn5Years(String annualizedReturnIn5Years) {
        this.annualizedReturnIn5Years = annualizedReturnIn5Years;
    }

    public String getNicheRating() {
        return nicheRating;
    }

    public void setNicheRating(String nicheRating) {
        this.nicheRating = nicheRating;
    }

    public String getNicheAreaFeel() {
        return nicheAreaFeel;
    }

    public void setNicheAreaFeel(String nicheAreaFeel) {
        this.nicheAreaFeel = nicheAreaFeel;
    }

    public String getNicheRentPercent() {
        return nicheRentPercent;
    }

    public void setNicheRentPercent(String nicheRentPercent) {
        this.nicheRentPercent = nicheRentPercent;
    }

    public String getNicheOwnPercent() {
        return nicheOwnPercent;
    }

    public void setNicheOwnPercent(String nicheOwnPercent) {
        this.nicheOwnPercent = nicheOwnPercent;
    }

    public String getNicheMedianRent() {
        return nicheMedianRent;
    }

    public void setNicheMedianRent(String nicheMedianRent) {
        this.nicheMedianRent = nicheMedianRent;
    }

    public String getNicheMedianHomeValue() {
        return nicheMedianHomeValue;
    }

    public void setNicheMedianHomeValue(String nicheMedianHomeValue) {
        this.nicheMedianHomeValue = nicheMedianHomeValue;
    }

    public String getNichePopulation() {
        return nichePopulation;
    }

    public void setNichePopulation(String nichePopulation) {
        this.nichePopulation = nichePopulation;
    }
}

