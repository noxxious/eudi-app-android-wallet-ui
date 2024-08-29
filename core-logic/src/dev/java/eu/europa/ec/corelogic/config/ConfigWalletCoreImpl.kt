/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific language
 * governing permissions and limitations under the Licence.
 */

package eu.europa.ec.corelogic.config

import android.content.Context
import eu.europa.ec.corelogic.BuildConfig
import eu.europa.ec.corelogic.controller.WalletCoreLogController
import eu.europa.ec.corelogic.util.ProvideKtorHttpClient
import eu.europa.ec.eudi.wallet.EudiWalletConfig
import eu.europa.ec.eudi.wallet.issue.openid4vci.OpenId4VciManager
import eu.europa.ec.eudi.wallet.transfer.openid4vp.ClientIdScheme
import eu.europa.ec.eudi.wallet.transfer.openid4vp.EncryptionAlgorithm
import eu.europa.ec.eudi.wallet.transfer.openid4vp.EncryptionMethod
import eu.europa.ec.eudi.wallet.transfer.openid4vp.PreregisteredVerifier
import eu.europa.ec.resourceslogic.R

internal class WalletCoreConfigImpl(
    private val context: Context,
    private val walletCoreLogController: WalletCoreLogController
) : WalletCoreConfig {

    private companion object {
        const val OPENID4VP_VERIFIER_API_URI = "https://snf-895762.vm.okeanos.grnet.gr"
        const val OPENID4VP_VERIFIER_LEGAL_NAME = "Hackathon verifier"
        const val OPENID4VP_VERIFIER_CLIENT_ID = "Verifier"
        const val OPENID4VP_PID_VERIFIER_API_URI = "https://192.168.8.107:8443/verifier-api"
        const val OPENID4VP_PID_VERIFIER_LEGAL_NAME = "Hackathon Pid verifier"
        const val OPENID4VP_PID_VERIFIER_CLIENT_ID = "pid_verifier"
        const val VCI_ISSUER_URL = "https://192.168.8.107:8443/issuer"
        const val VCI_CLIENT_ID = "wallet-dev"
        const val AUTHENTICATION_REQUIRED = false
    }

    private var _config: EudiWalletConfig? = null

    override val config: EudiWalletConfig
        get() {
            if (_config == null) {
                _config = EudiWalletConfig.Builder(context)
                    .logger(walletCoreLogController)
                    .userAuthenticationRequired(AUTHENTICATION_REQUIRED)
                    .openId4VpConfig {
                        withEncryptionAlgorithms(listOf(EncryptionAlgorithm.ECDH_ES))
                        withEncryptionMethods(
                            listOf(
                                EncryptionMethod.A128CBC_HS256,
                                EncryptionMethod.A256GCM
                            )
                        )

                        withClientIdSchemes(
                            listOf(
                                ClientIdScheme.X509SanDns,
                                ClientIdScheme.Preregistered(
                                    listOf(
                                        PreregisteredVerifier(
                                            clientId = OPENID4VP_VERIFIER_CLIENT_ID,
                                            verifierApi = OPENID4VP_VERIFIER_API_URI,
                                            legalName = OPENID4VP_VERIFIER_LEGAL_NAME
                                        ),
                                        PreregisteredVerifier(
                                            clientId = OPENID4VP_PID_VERIFIER_CLIENT_ID,
                                            verifierApi = OPENID4VP_PID_VERIFIER_API_URI,
                                            legalName = OPENID4VP_PID_VERIFIER_LEGAL_NAME
                                        )
                                    )
                                )
                            )
                        )
                        withScheme(
                            listOf(
                                BuildConfig.OPENID4VP_SCHEME,
                                BuildConfig.EUDI_OPENID4VP_SCHEME,
                                BuildConfig.MDOC_OPENID4VP_SCHEME
                            )
                        )
                    }
                    .openId4VciConfig {
                        issuerUrl(issuerUrl = VCI_ISSUER_URL)
                        clientId(clientId = VCI_CLIENT_ID)
                        authFlowRedirectionURI(BuildConfig.ISSUE_AUTHORIZATION_DEEPLINK)
                        useStrongBoxIfSupported(true)
                        useDPoP(true)
                        parUsage(OpenId4VciManager.Config.ParUsage.IF_SUPPORTED)
                        proofTypes(
                            OpenId4VciManager.Config.ProofType.JWT,
                            OpenId4VciManager.Config.ProofType.CWT
                        )
                    }
                    .trustedReaderCertificates(R.raw.eudi_pid_issuer_ut)
                    .ktorHttpClientFactory {
                        ProvideKtorHttpClient.client()
                    }
                    .build()
            }
            return _config!!
        }
}