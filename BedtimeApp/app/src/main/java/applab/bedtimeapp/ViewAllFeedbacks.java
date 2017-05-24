package applab.bedtimeapp;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;


import java.util.List;

import applab.bedtimeapp.db.FeedbackOperations;
import applab.bedtimeapp.model.Feedback;

public class ViewAllFeedbacks extends ListActivity{

    private FeedbackOperations feedbackOps;
    List<Feedback> feedbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_feedbacks);
        feedbackOps = new FeedbackOperations(this);
        feedbackOps.open();
        feedbacks = feedbackOps.getAllFeedbacks(-1);
        feedbackOps.close();
        ArrayAdapter<Feedback> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, feedbacks);
        setListAdapter(adapter);
    }
}