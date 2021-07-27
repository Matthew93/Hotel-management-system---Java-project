package hotel.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Rooms {
	@Id
	int id;

	@Column(name = "roomNumber")
	String roomNumber;

	@Column(name = "price")
	Double price;

	@Column(name = "available")
	Boolean available;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "roomID")
	List<Review> reviews;

	@Enumerated(EnumType.STRING)
	RoomType roomType;

	public Rooms() {

	}

	public Rooms(String roomNumber, Double price, Boolean available, RoomType roomType) {
		this.roomNumber = roomNumber;
		this.price = price;
		this.available = available;
		this.roomType = roomType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public void addReview(Review theReview) {
		if (reviews == null) {
			reviews = new ArrayList<>();

		}

		reviews.add(theReview);
	}
}
