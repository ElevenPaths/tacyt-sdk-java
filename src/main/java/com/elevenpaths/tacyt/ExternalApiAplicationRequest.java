/*Tacyt Java SDK - Set of  reusable classes to  allow developers integrate Tacyt on their applications.
Copyright (C) 2013 Eleven Paths

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA*/

package com.elevenpaths.tacyt;

import java.util.Date;

public class ExternalApiAplicationRequest extends ExternalApiRequest {

    private String uniqueOriginId;
    private String uniqueVersionId;
    private String platform;
    private String origin;
    private String title;
    private String description;
    private String developerName;
    private String appURL;
    private String packageName;
    private String versionString;
    private Integer versionCode;
    private String developerId;
    private String developerEmail;
    private String developerWeb;
    private String developerAddress;
    private String developerPrivacy;
    private Date uploadDate;
    private String[] genreNames;
    private Integer numDownloads;
    private Float price;
    private String currencyCode;
    private String seller;
    private String supportUrl;
    private String categoryName;
    private Long marketSize;
    private String applicationType;
    private String iosAppId;
    private String appId;
    private String directDownloadURL;
    private String linkToOfficialMarket;
    private Date findDate;
    private Date deadDate;

    public ExternalApiAplicationRequest(String uniqueOriginId, String uniqueVersionId, String platform, String origin, String title, String appURL, Date findDate) {
        this.uniqueOriginId = uniqueOriginId;
        this.uniqueVersionId = uniqueVersionId;
        this.platform = platform;
        this.origin = origin;
        this.title = title;
        this.appURL = appURL;
        this.findDate = findDate;
    }

    public String getUniqueOriginId() {
        return uniqueOriginId;
    }

    public void setUniqueOriginId(String uniqueOriginId) {
        this.uniqueOriginId = uniqueOriginId;
    }

    public String getUniqueVersionId() {
        return uniqueVersionId;
    }

    public void setUniqueVersionId(String uniqueVersionId) {
        this.uniqueVersionId = uniqueVersionId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
    }

    public String getDeveloperWeb() {
        return developerWeb;
    }

    public void setDeveloperWeb(String developerWeb) {
        this.developerWeb = developerWeb;
    }

    public String getDeveloperAddress() {
        return developerAddress;
    }

    public void setDeveloperAddress(String developerAddress) {
        this.developerAddress = developerAddress;
    }

    public String getDeveloperPrivacy() {
        return developerPrivacy;
    }

    public void setDeveloperPrivacy(String developerPrivacy) {
        this.developerPrivacy = developerPrivacy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String[] getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(String[] genreNames) {
        this.genreNames = genreNames;
    }

    public Integer getNumDownloads() {
        return numDownloads;
    }

    public void setNumDownloads(Integer numDownloads) {
        this.numDownloads = numDownloads;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(String supportUrl) {
        this.supportUrl = supportUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getMarketSize() {
        return marketSize;
    }

    public void setMarketSize(Long marketSize) {
        this.marketSize = marketSize;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getIosAppId() {
        return iosAppId;
    }

    public void setIosAppId(String iosAppId) {
        this.iosAppId = iosAppId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDirectDownloadURL() {
        return directDownloadURL;
    }

    public void setDirectDownloadURL(String directDownloadURL) {
        this.directDownloadURL = directDownloadURL;
    }

    public String getLinkToOfficialMarket() {
        return linkToOfficialMarket;
    }

    public void setLinkToOfficialMarket(String linkToOfficialMarket) {
        this.linkToOfficialMarket = linkToOfficialMarket;
    }

    public Date getFindDate() {
        return findDate;
    }

    public void setFindDate(Date findDate) {
        this.findDate = findDate;
    }

    public Date getDeadDate() {
        return deadDate;
    }

    public void setDeadDate(Date deadDate) {
        this.deadDate = deadDate;
    }
}
