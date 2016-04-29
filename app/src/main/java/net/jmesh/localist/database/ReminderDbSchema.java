package net.jmesh.localist.database;

public class ReminderDbSchema {
    public static final class ReminderTable {
        public static final String NAME = "reminders";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String TYPE = "type";
            public static final String CONTENT = "content";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DATE = "date";
        }
    }
}
