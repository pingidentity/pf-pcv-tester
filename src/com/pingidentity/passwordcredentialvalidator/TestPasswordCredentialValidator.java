package com.pingidentity.passwordcredentialvalidator;

import java.util.Collections;

import org.sourceid.saml20.adapter.attribute.AttributeValue;
import org.sourceid.saml20.adapter.conf.Configuration;
import org.sourceid.saml20.adapter.gui.TextFieldDescriptor;
import org.sourceid.saml20.adapter.gui.validation.impl.RequiredFieldValidator;
import org.sourceid.util.log.AttributeMap;

import com.pingidentity.sdk.GuiConfigDescriptor;
import com.pingidentity.sdk.PluginDescriptor;
import com.pingidentity.sdk.password.PasswordCredentialValidator;
import com.pingidentity.sdk.password.PasswordValidationException;

public class TestPasswordCredentialValidator implements PasswordCredentialValidator
{
    private static String USERNAME = "Username";
    private static String BAD_PASSWORD = "Bad Password";
    private static String TYPE = "Test Password Credential Validator";

    String bad_password = null;

    @Override
    public void configure(Configuration configuration)
    {
        this.bad_password = configuration.getFieldValue(BAD_PASSWORD);
    }

    @Override
    public PluginDescriptor getPluginDescriptor()
    {
        GuiConfigDescriptor guiDescriptor = new GuiConfigDescriptor();
        guiDescriptor.setDescription(TYPE);

        TextFieldDescriptor badpassFieldDescriptor = new TextFieldDescriptor(BAD_PASSWORD, "Value to result in a failed authn (everything else will succeed)");
        guiDescriptor.addField(badpassFieldDescriptor);

        PluginDescriptor pluginDescriptor = new PluginDescriptor(TYPE, this, guiDescriptor);
        pluginDescriptor.setAttributeContractSet(Collections.singleton(USERNAME));
        pluginDescriptor.setSupportsExtendedContract(false);
        return pluginDescriptor;
    }

    @Override
    public AttributeMap processPasswordCredential(String username, String password) throws PasswordValidationException
    {
        AttributeMap attributeMap = null;

		if (!this.bad_password.equalsIgnoreCase(password)) {
            attributeMap = new AttributeMap();
            attributeMap.put(USERNAME, new AttributeValue(username));
        } else {
            // authentication failed return null or an empty map
            return null;
        }

        return attributeMap;
    }
}