
enum ComplaintStatus {
    Pending,Resolved;
}

public class Complaint {

    private final String complaintDescription;
    private String resolution;
    private ComplaintStatus status;

    public Complaint(String complaintDescription) {
        this.complaintDescription = complaintDescription;
        this.status = ComplaintStatus.Pending;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public void getComplaint() {

        System.out.println("Description : " + getComplaintDescription());
        if(status == ComplaintStatus.Resolved) {
            System.out.println("Resolution : " + getResolution());
        }
        System.out.println("Status : " + getStatus());

    }

}


