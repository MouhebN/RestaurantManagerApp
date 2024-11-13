package tn.esprit.restaurantmanagerapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class AddReservationActivityTest {

    @Rule
    public ActivityScenarioRule<AddReservationActivity> activityRule = new ActivityScenarioRule<>(AddReservationActivity.class);

    private Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testAddReservation() {
        // Input values for adding a new reservation
        onView(withId(R.id.editTextCustomerId)).perform(typeText("123"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextReservationDate)).perform(typeText("2024-11-15"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextReservationTime)).perform(typeText("18:30"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPartySize)).perform(typeText("4"), ViewActions.closeSoftKeyboard());

        // Click the "Add Reservation" button
        onView(withId(R.id.buttonAddReservation)).perform(click());

        // Register IdlingResource for RecyclerView
        RecyclerViewIdlingResource recyclerViewIdlingResource = new RecyclerViewIdlingResource(activityRule, R.id.recyclerViewReservations);
        Espresso.registerIdlingResources(recyclerViewIdlingResource);

        // Check that the RecyclerView displays the added reservation with the expected customer ID
        onView(withId(R.id.recyclerViewReservations))
                .check(matches(ViewMatchers.hasDescendant(withText("123"))));

        // Unregister the IdlingResource
        Espresso.unregisterIdlingResources(recyclerViewIdlingResource);
    }

    // Custom IdlingResource to wait until the RecyclerView is populated
    public static class RecyclerViewIdlingResource implements IdlingResource {
        private ResourceCallback resourceCallback;
        private final ActivityScenarioRule<AddReservationActivity> activityScenarioRule;
        private final int recyclerViewId;

        public RecyclerViewIdlingResource(ActivityScenarioRule<AddReservationActivity> activityScenarioRule, int recyclerViewId) {
            this.activityScenarioRule = activityScenarioRule;
            this.recyclerViewId = recyclerViewId;
        }

        @Override
        public String getName() {
            return RecyclerViewIdlingResource.class.getName();
        }

        @Override
        public boolean isIdleNow() {
            final boolean[] isIdle = {false};
            activityScenarioRule.getScenario().onActivity(activity -> {
                RecyclerView recyclerView = activity.findViewById(recyclerViewId);
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    isIdle[0] = recyclerView.getAdapter().getItemCount() > 0;
                    if (isIdle[0] && resourceCallback != null) {
                        resourceCallback.onTransitionToIdle();
                    }
                }
            });
            return isIdle[0];
        }

        @Override
        public void registerIdleTransitionCallback(@NonNull ResourceCallback callback) {
            this.resourceCallback = callback;
        }
    }
}
