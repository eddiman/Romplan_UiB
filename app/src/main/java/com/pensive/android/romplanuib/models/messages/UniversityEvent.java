package com.pensive.android.romplanuib.models.messages;

import com.pensive.android.romplanuib.models.University;

public class UniversityEvent {

    private final University university;

    public UniversityEvent(University university) {
        this.university = university;
    }

    public University getUniversity() {
        return university;
    }
}
