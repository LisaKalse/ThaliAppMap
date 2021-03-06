package com.example.AppArt.thaliapp.Calendar.Backend;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.AppArt.thaliapp.Calendar.Backend.EventCategory;
import com.example.AppArt.thaliapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Withholds all knowledge of a ThaliaEvent
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class ThaliaEvent implements Comparable<ThaliaEvent>, Parcelable {

    private final String startDate;
    private final String endDate;

    private final String location;
    private final String description;
    private final String summary;
    private final EventCategory category;
    private final int catIcon;

    private String datum;
    private String begintime;
    private String endtime;

    /**
     * Initialises the Event object given string input.
     *
     * @param startDate     The starting time of the event in DATE-TIME format
     * @param endDate       The ending time of the event in DATE-TIME format
     * @param location      The location of the event
     * @param description   A large description of the event
     * @param summary       The event in 3 words or fewer
     */
    public ThaliaEvent(String startDate, String endDate, String location,
                       String description, String summary) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.summary = summary;
        this.category = categoryFinder(description);
        this.catIcon = catIconFinder(category);
        setAll();
    }

    /**
     * Parses an iCalendar date string(DATE-TIME) to tiny bits which are never
     * returned.
     *
     * @param date Time in a DATE-TIME format
     * @return Time in GregorianCalendar format
     */
    private GregorianCalendar dateFinder(String date) {
        String temp = date.substring(0, 4);
        int year = Integer.parseInt(temp);
        temp = date.substring(4, 6);
        // GregorianCalendar has January = 0, iCal has January = 1
        // This is what makes programming fun!
        int month = Integer.parseInt(temp) - 1;
        temp = date.substring(6, 8);
        int day = Integer.parseInt(temp);
        temp = date.substring(9, 11);
        int hour = Integer.parseInt(temp);
        temp = date.substring(11, 13);
        int minute = Integer.parseInt(temp);
        int second = 0;
        GregorianCalendar calDate = new GregorianCalendar();
        calDate.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        calDate.set(year, month, day, hour, minute, second);
        return calDate;
    }

    /**
     * Uses the description of an ThaliaEvent to figure out what category it is.
     *
     * @param description a string in where the category is given
     * @return an EventCategory
     */
    private EventCategory categoryFinder(String description) {
        if (description.contains("borrel")) {
            return EventCategory.BORREL;
        } else if (description.contains("lezing")) {
            return EventCategory.LECTURE;
        } else if (description.contains("feest") || description.contains("party")) {
            return EventCategory.PARTY;
        } else if (description.contains("alv") || description.contains("ALV")) {
            return EventCategory.ALV;
        } else if (description.contains("workshop")) {
            return EventCategory.WORKSHOP;
        } else return EventCategory.DEFAULT;
    }

    /**
     * @param cat the category of this event
     * @return A .png file that represents the category of this event
     */
    private int catIconFinder(EventCategory cat) {
        int catIcon;
        switch (cat) {
            case ALV:
                catIcon = R.drawable.alvicoon;
                break;
            case BORREL:
                catIcon = R.drawable.borrelicoon;
                break;
            case LECTURE:
                catIcon = R.drawable.lezingicoon;
                break;
            case PARTY:
                catIcon = R.drawable.feesticoon;
                break;
            case WORKSHOP:
                catIcon = R.drawable.workshopicoon;
                break;
            default:
                catIcon = R.drawable.overigicoon;
        }
        return catIcon;
    }

    /**
     * Printfunction, useful when you're debugging
     *
     * @return a string of the event
     */
    @Override
    public String toString() {
        return ("\nstart = " + startDate + ", end = " + endDate
                + "\nlocation = " + location + "\ndescription = " + description
                + "\nsummary = " + summary);
    }

    /*****************************************************************
    Getters for all attributes
     *****************************************************************/
    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getSummary() {
        return summary;
    }

    public EventCategory getCategory() {
        return category;
    }

    public int getCatIcon() {
        return catIcon;
    }

    public String makeSummary() {
        return summary + "\n" + duration() + "\n" + location;
    }

    /**
     * @return Gregorian Calendar format of the StringDate format
     */
    public GregorianCalendar getGregCalFormat(String date) {
        return dateFinder(date);
    }

    /**
     * Creates small strings representing time in a userfriendly way
     */
    private void setAll() {
        datum = getGregCalFormat(startDate).getTime().toString().substring(0, 10);
        begintime = getGregCalFormat(startDate).getTime().toString().substring(11, 16);
        endtime = getGregCalFormat(endDate).getTime().toString().substring(11, 16);
    }

    public String getDatumString() {
        return datum;
    }

    public String getBeginTime() {
        return begintime;
    }

    public String getEndTime() {
        return endtime;
    }

    /**
     * A neat stringformat of the beginning and ending times
     *
     * @return hh:mm-hh:mm
     */
    public String duration() {
        StringBuilder sb = new StringBuilder();
        sb.append(getGregCalFormat(startDate).get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        if (getGregCalFormat(startDate).get(Calendar.MINUTE) == 0) {
            sb.append("00");
        } else {
            sb.append(getGregCalFormat(startDate).get(Calendar.MINUTE));
        }
        sb.append(" - ");
        sb.append(getGregCalFormat(endDate).get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        if (getGregCalFormat(endDate).get(Calendar.MINUTE) == 0) {
            sb.append("00");
        } else {
            sb.append(getGregCalFormat(endDate).get(Calendar.MINUTE));
        }
        return sb.toString();
    }

    /**
     * @param another the ThaliaEvent with wich you want to compare it
     * @return The difference in time between the two
     */
    // TODO Greg Cal is er uit gesloopd, dus nu fixen op basis van Strings
    @Override
    public int compareTo(@NonNull ThaliaEvent another) {
        return startDate.compareTo(another.startDate);
    }

    /*****************************************************************
    Making it a Parcelable, so it can be passed through with an intent
     *****************************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Pretty much all information about this ThaliaEvent object is being
     * compressed into a Parcel
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(endDate);

        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(summary);
    }

    /**
     * Reconstructs the ThaliaEvent through a parcel.
     */
    public static final Parcelable.Creator<ThaliaEvent> CREATOR
            = new Parcelable.Creator<ThaliaEvent>() {
        // Parcels work FIFO, so we do this the other way around
        public ThaliaEvent createFromParcel(Parcel parcel) {
            String startDate = parcel.readString();
            String endDate = parcel.readString();
            String location = parcel.readString();
            String description = parcel.readString();
            String summary = parcel.readString();
            return new ThaliaEvent(startDate, endDate, location, description, summary);
        }

        public ThaliaEvent[] newArray(int size) {
            return new ThaliaEvent[size];
        }
    };
}
