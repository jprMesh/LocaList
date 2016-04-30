package net.jmesh.localist.database;

public class ReminderDbSchema {
    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CONTENT = "content";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DATE = "date";
        }
    }

    public static final class ListTable {
        public static final String NAME = "lists";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CONTENT = "content";
            public static final String ACTIVITY = "activity";
        }
    }
}
