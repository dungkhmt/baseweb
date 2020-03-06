package hust.baseweb.baseweb.model;

import java.util.Date;
import java.util.UUID;

public interface GetUserLogin {
    UUID getId();
    String getUsername();
    Date getCreatedAt();
    Date getUpdatedAt();
}
