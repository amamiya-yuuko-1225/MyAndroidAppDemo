package com.experiment.todolist;

public class DateData {
        private int year;
        private int []month = {31,28,31,30,31,30,31,31,30,31,30,31};
        public void setYear(int year) {
            this.year=year;
        }
        public int getMonth(int i) {
            if ((year % 100 == 0 && year % 400 == 0) || year % 4 == 0)
                month[1] = 29;
            else
                month[1] = 28;
            return month[i];
        }

}
