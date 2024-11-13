package tn.esprit.restaurantmanagerapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.Espresso;
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
public class OrderActivityTest {

    @Rule
    public ActivityScenarioRule<OrderActivity> activityRule = new ActivityScenarioRule<>(OrderActivity.class);

    private Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testAddOrder() {
        // Input values for adding a new order
        onView(withId(R.id.editTextOrderType)).perform(typeText("Dine-In"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextOrderStatus)).perform(typeText("Pending"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextTotalPrice)).perform(typeText("100.0"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextCustomerId)).perform(typeText("123"), ViewActions.closeSoftKeyboard());

        // Click the "Add Order" button
        onView(withId(R.id.buttonAddUpdateOrder)).perform(click());

        // Register RecyclerViewIdlingResource to wait for RecyclerView to update
        RecyclerViewIdlingResource recyclerViewIdlingResource = new RecyclerViewIdlingResource(activityRule, R.id.recyclerViewOrders);
        Espresso.registerIdlingResources(recyclerViewIdlingResource);

        // Check that the RecyclerView contains the added order with "Type: Dine-In" format
        onView(withId(R.id.recyclerViewOrders))
                .check(matches(ViewMatchers.hasDescendant(withText("Type: Dine-In"))));

        // Unregister the IdlingResource
        Espresso.unregisterIdlingResources(recyclerViewIdlingResource);
    }

    // IdlingResource to wait for RecyclerView to populate
    public static class RecyclerViewIdlingResource implements IdlingResource {
        private ResourceCallback resourceCallback;
        private final ActivityScenarioRule<OrderActivity> activityScenarioRule;
        private final int recyclerViewId;

        public RecyclerViewIdlingResource(ActivityScenarioRule<OrderActivity> activityScenarioRule, int recyclerViewId) {
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
