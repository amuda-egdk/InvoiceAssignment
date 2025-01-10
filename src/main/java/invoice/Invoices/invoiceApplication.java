package invoice.Invoices;

import org.dalesbred.Database;

import invoice.Invoices.resources.InvoiceResources;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class invoiceApplication extends Application<invoiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new invoiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "invoice";
    }

    @Override
    public void initialize(final Bootstrap<invoiceConfiguration> bootstrap) {
        // TODO: application initialization
    	bootstrap.addBundle(new SwaggerBundle<invoiceConfiguration>() {

			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(invoiceConfiguration configuration) {
				return configuration.swaggerBundleConfiguration;
			}
		});
    }

    @Override
    public void run(final invoiceConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    	DataSourceFactory config = configuration.getDataSourceFactory();
		final Database database = Database.forUrlAndCredentials(config.getUrl(), config.getUser(),
				config.getPassword());
		
		environment.jersey().register(new InvoiceResources(database));    
		}    
}
