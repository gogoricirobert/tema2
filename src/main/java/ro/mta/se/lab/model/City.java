package ro.mta.se.lab.model;

public class City {
    String mName;
    String mCountry;
    int mId;
    String mLat;
    String mLon;

    public City(String name,String country,int id, String lat,String lon)
    {
        this.mName=name;
        this.mCountry=country;
        this.mId=id;
        this.mLat=lat;
        this.mLon=lon;
    }

    public String getName(){return mName;}

    public String getCountry(){return mCountry;}

    public int getId(){return mId;}


    public String getLat(){return mLat;}

    public String mLon(){return mLon;}
}
