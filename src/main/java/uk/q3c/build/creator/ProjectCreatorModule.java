package uk.q3c.build.creator;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import uk.q3c.build.creator.gradle.GradleGroovyFeatureBuilder;

/**
 * Created by David Sowerby on 10 Oct 2016
 */
public class ProjectCreatorModule extends AbstractModule {

    private Multibinder<FeatureBuilder> builders;

    @Override
    protected void configure() {
        builders = Multibinder.newSetBinder(binder(), FeatureBuilder.class);
        builders.addBinding().toInstance(new GradleGroovyFeatureBuilder());
        bind(FeatureBuilders.class);
        bind(ProjectCreator.class).to(DefaultProjectCreator.class);

    }
}
