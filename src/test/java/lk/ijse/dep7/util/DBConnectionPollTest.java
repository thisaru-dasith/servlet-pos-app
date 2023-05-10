package lk.ijse.dep7.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionPollTest {

    @Test
    void getConnection() {
        assertNotNull(DBConnectionPoll.getConnection());
    }


}