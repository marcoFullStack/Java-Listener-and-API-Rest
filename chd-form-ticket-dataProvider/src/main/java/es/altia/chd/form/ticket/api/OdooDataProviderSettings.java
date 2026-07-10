package es.altia.chd.form.ticket.api;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderParameterSettings;

@DDMForm
@DDMFormLayout(
        {
                @DDMFormLayoutPage(
                        {
                                @DDMFormLayoutRow(
                                        {
                                                @DDMFormLayoutColumn(
                                                        size = 12, value = "outputParameters"
                                                )
                                        }
                                )
                        }
                )
        }
)
public interface OdooDataProviderSettings extends DDMDataProviderParameterSettings {
}