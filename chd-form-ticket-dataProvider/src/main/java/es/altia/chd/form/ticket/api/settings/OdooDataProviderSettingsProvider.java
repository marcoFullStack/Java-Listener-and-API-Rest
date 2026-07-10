package es.altia.chd.form.ticket.api.settings;

// 🌟 CORREGIDO: Quitamos el ".settings" del final del paquete de Liferay
import com.liferay.dynamic.data.mapping.data.provider.settings.DDMDataProviderSettingsProvider;
import es.altia.chd.form.ticket.api.OdooDataProviderSettings;
import org.osgi.service.component.annotations.Component;

@Component(
        property = "ddm.data.provider.type=odoo-data-provider-final",
        service = DDMDataProviderSettingsProvider.class
)
public class OdooDataProviderSettingsProvider implements DDMDataProviderSettingsProvider {

    @Override
    public Class<?> getSettings() {
        return OdooDataProviderSettings.class;
    }
}
