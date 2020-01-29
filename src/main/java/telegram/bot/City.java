package telegram.bot;

import javax.persistence.*;

@Entity
@Table(name = "telegram_bot")
public class City {
    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name="city")
    private String name;

    @Column(name="description")
    private String description;

    public City() {}

    public City(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
