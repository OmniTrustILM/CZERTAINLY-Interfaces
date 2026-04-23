package com.czertainly.api.model.core.connector;

import com.czertainly.api.clients.ApiClientConnectorInfo;
import com.czertainly.api.model.client.attribute.ResponseAttribute;
import com.czertainly.api.model.common.NameAndUuidDto;
import com.czertainly.api.model.core.proxy.ProxyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ConnectorDto extends NameAndUuidDto implements ApiClientConnectorInfo {

    @Schema(description = "List of Function Groups implemented by the Connector",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FunctionGroupDto> functionGroups;
    @Schema(description = "URL of the Connector",
            examples = {"http://network-discovery-provider:8080"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
    @Schema(description = "Type of authentication for the Connector",
            examples = {"none"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private AuthType authType;
    @Schema(description = "List of Attributes for the authentication type",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ResponseAttribute> authAttributes;
    @Schema(description = "Status of the Connector",
            examples = {"CONNECTED"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private ConnectorStatus status;
    @Schema(description = "Proxy for message queue routing. " +
            "When set, connector communicates via message queue proxy. " +
            "When null, connector uses direct REST communication.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ProxyDto proxy;
    @Schema(description = "List of Custom Attributes")
    private List<ResponseAttribute> customAttributes;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("functionGroups", functionGroups)
                .append("url", url)
                .append("authType", authType)
                .append("authAttributes", authAttributes)
                .append("status", status)
                .append("proxy", proxy)
                .append("name", name)
                .append("uuid", uuid)
                .append("customAttributes", customAttributes)
                .toString();
    }
}
