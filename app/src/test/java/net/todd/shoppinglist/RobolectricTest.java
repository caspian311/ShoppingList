package net.todd.shoppinglist;

import android.app.Activity;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(CustomRobolectricRunner.class)
public class RobolectricTest {
    @Test
    public void testIt() {
        Activity activity =
                Robolectric.setupActivity(MainActivity.class);

        TextView results =
                (TextView) activity.findViewById(R.id.textView);
        String resultsText = results.getText().toString();

        // failing test gives much better feedback
        // to show that all works correctly ;)
        assertThat(resultsText, equalTo("Testing Android Rocks!"));
    }
}
