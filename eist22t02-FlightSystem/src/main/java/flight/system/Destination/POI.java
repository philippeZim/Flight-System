package flight.system.Destination;

public abstract class POI {
    private String imageLink;
    private String tripAdvisorLink;
    private String name;

    public POI(String imageLink, String tripAdvisorLink, String name) {
        this.imageLink = imageLink;
        this.tripAdvisorLink = tripAdvisorLink;
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTripAdvisorLink() {
        return tripAdvisorLink;
    }

    public void setTripAdvisorLink(String tripAdvisorLink) {
        this.tripAdvisorLink = tripAdvisorLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return "Name: " + getName() + ", Link: " + getTripAdvisorLink() + ", Image Link: " + getImageLink();
    }
}
