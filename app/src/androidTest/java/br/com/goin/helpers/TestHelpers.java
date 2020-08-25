package br.com.goin.helpers;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import br.com.goin.RetrofitService.TestRetrofitService;
import br.com.goin.model.terms.TermsApi;
import br.com.goin.model.terms.TermsRepository;
import br.com.goin.model.follow.FollowApi;
import br.com.goin.model.follow.FollowRepository;

public class TestHelpers {
    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public static void mockFollow() {
        FollowRepository.Companion.setService(new TestRetrofitService<>(FollowApi.class, Constants.GRAPHQL_BASE_URL, new TestRetrofitService.OfflineMockInterceptor()));
    }

    public static void mockTerms() {
        TermsRepository.Companion.setService(new TestRetrofitService<>(TermsApi.class, Constants.GRAPHQL_BASE_URL, new TestRetrofitService.OfflineMockInterceptor()));
    }
}
