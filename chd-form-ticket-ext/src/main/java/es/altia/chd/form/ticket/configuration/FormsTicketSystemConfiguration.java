package es.altia.chd.form.ticket.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
	category = "chd-forms-ticket",
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
    id = "es.altia.chd.form.ticket.configuration.FormsTicketSystemConfiguration",
    localization = "content/Language",
    name = "forms-ticket-system-configuration"
)
public interface FormsTicketSystemConfiguration {

    @Meta.AD(
        deflt = "",
        description = "forms-ticket-configurations-description",
        name = "forms-ticket-configurations",
        required = false
    )
    public String[] formsTicketConfigurations();
}