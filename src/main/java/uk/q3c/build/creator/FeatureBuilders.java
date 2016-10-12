package uk.q3c.build.creator;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Created by David Sowerby on 10 Oct 2016
 */
public class FeatureBuilders {

    private Set<FeatureBuilder> builders;

    @Inject
    public FeatureBuilders(Set<FeatureBuilder> builders) {
        this.builders = builders;
    }

    public int builderCount() {
        return builders.size();
    }

    @Nonnull
    public ImmutableSet<FeatureBuilder> getBuilders() {
        return ImmutableSet.copyOf(builders);
    }
}
