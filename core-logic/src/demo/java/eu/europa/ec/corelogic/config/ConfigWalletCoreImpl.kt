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
import eu.europa.ec.eudi.wallet.EudiWalletConfig
import eu.europa.ec.eudi.wallet.issue.openid4vci.OpenId4VciManager
import eu.europa.ec.eudi.wallet.transfer.openId4vp.ClientIdScheme
import eu.europa.ec.eudi.wallet.transfer.openId4vp.EncryptionAlgorithm
import eu.europa.ec.eudi.wallet.transfer.openId4vp.EncryptionMethod
import eu.europa.ec.eudi.wallet.transfer.openId4vp.Format
import eu.europa.ec.eudi.wallet.transfer.openId4vp.PreregisteredVerifier
import eu.europa.ec.resourceslogic.R

internal class WalletCoreConfigImpl(
    private val context: Context
) : WalletCoreConfig {

    private companion object {
        const val OPENID4VP_VERIFIER_API_URI = "https://verifier-api.eudiw-lt.lengor.dev/"
        const val OPENID4VP_VERIFIER_LEGAL_NAME = "LT Potential Test verifier"
        const val OPENID4VP_VERIFIER_CLIENT_ID = "Verifier"
        const val VCI_ISSUER_URL = "https://issuer.eudiw-lt.lengor.dev/"
        const val VCI_CLIENT_ID = "wallet-dev"
        const val AUTHENTICATION_REQUIRED = false
    }

    private var _config: EudiWalletConfig? = null

    override val config: EudiWalletConfig
        get() {
            if (_config == null) {
                _config = EudiWalletConfig {
                    configureDocumentKeyCreation(
                        userAuthenticationRequired = AUTHENTICATION_REQUIRED,
                        userAuthenticationTimeout = 30_000L,
                        useStrongBoxForKeys = true
                    )
                    configureOpenId4Vp {
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
                                        )
                                    )
                                )
                            )
                        )
                        withSchemes(
                            listOf(
                                BuildConfig.OPENID4VP_SCHEME,
                                BuildConfig.EUDI_OPENID4VP_SCHEME,
                                BuildConfig.MDOC_OPENID4VP_SCHEME,
                                // Additional scheme seen in WP-3/UC-4 Utrecht tests
                                "mdoc",
                            )
                        )
                        withFormats(
                            Format.MsoMdoc, Format.SdJwtVc.ES256
                        )
                    }

                    configureOpenId4Vci {
                        withIssuerUrl(issuerUrl = VCI_ISSUER_URL)
                        withClientId(clientId = VCI_CLIENT_ID)
                        withAuthFlowRedirectionURI(BuildConfig.ISSUE_AUTHORIZATION_DEEPLINK)
                        withParUsage(OpenId4VciManager.Config.ParUsage.IF_SUPPORTED)
                        withUseDPoPIfSupported(true)
                    }
                    configureReaderTrustStore(
                        context,
                        R.raw.eudi_pid_issuer_ut,
                        R.raw.potential_tollgate_verifier,
                        R.raw.slovenia_rp_verifier,
                        R.raw.czechia_rp_verifier,
                        R.raw.iaca_lt_potential,
                        // mDL Test event IACA's
                        R.raw.iaca_ama,
                        R.raw.iaca_animo_eu_pid,
                        R.raw.iaca_animo_mdl,
                        //R.raw.iaca_apple,
                        R.raw.iaca_bdr,
                        //R.raw.iaca_bosa,
                        //R.raw.iaca_bundesdruckerei,
                        R.raw.iaca_clear,
                        R.raw.iaca_clr_labs_0,
                        R.raw.iaca_clr_labs_1,
                        R.raw.iaca_clr_labs_2,
                        R.raw.iaca_coi_poland,
                        R.raw.iaca_credence_id,
                        R.raw.iaca_diia,
                        R.raw.iaca_explicit_selection,
                        R.raw.iaca_felica,
                        R.raw.iaca_fime_eu_pid,
                        R.raw.iaca_fime_icao,
                        R.raw.iaca_fime_mdl_1,
                        R.raw.iaca_fime_mdl_2,
                        R.raw.iaca_fime_mvr,
                        R.raw.iaca_france_identite_test,
                        R.raw.iaca_google_mdl,
                        R.raw.iaca_google,
                        R.raw.iaca_grnet,
                        R.raw.iaca_hid,
                        R.raw.iaca_idakto,
                        R.raw.iaca_idemia,
                        R.raw.iaca_ipzs,
                        R.raw.iaca_lt,
                        R.raw.iaca_luxembourg_qua,
                        R.raw.iaca_luxembourg_tst,
                        R.raw.iaca_nortal,
                        R.raw.iaca_ogcio,
                        R.raw.iaca_oidf,
                        R.raw.iaca_panasonic,
                        R.raw.iaca_procivis,
                        R.raw.iaca_rdw_eu_pid,
                        R.raw.iaca_rdw_mdl,
                        R.raw.iaca_reaktor,
                        R.raw.iaca_samsung,
                        R.raw.iaca_scytales,
                        R.raw.iaca_sicpa,
                        R.raw.iaca_spruceid,
                        R.raw.iaca_toppan_mdl,
                        R.raw.iaca_veridos,
                        R.raw.iaca_zetes,
                        R.raw.intermediate_iaca_luxembourg_qua,
                        R.raw.intermediate_iaca_luxembourg_tst,
                        // Reader certs
                        R.raw.animo_reader_ca,
                        R.raw.bundesdruckerei_reader_ca,
                        R.raw.clr_labs_reader_ca,
                        R.raw.credence_id_reader_ca,
                        R.raw.fast_enterprises_reader_ca,
                        R.raw.fime_reader_ca_1,
                        R.raw.fime_reader_ca_2,
                        R.raw.google_reader_ca,
                        R.raw.idakto_reader_ca,
                        R.raw.idemia_reader_ca,
                        R.raw.in_groupe_reader_ca,
                        R.raw.lapid_reader_ca,
                        R.raw.lt_reader_ca,
                        R.raw.mattr_reader_ca,
                        R.raw.nearform_reader_ca,
                        R.raw.nist_readerca,
                        R.raw.ogcio_reader_ca,
                        R.raw.panasonic_reader_ca,
                        R.raw.rdw_test_reader_ca,
                        R.raw.scytales_reader_ca,
                        R.raw.spruceid_reader_ca,
                        R.raw.thales_reader_ca_1,
                        R.raw.thales_reader_ca_2,
                        R.raw.thales_root_ca,
                        R.raw.toppan_reader_ca,
                        R.raw.zetes_reader_ca,
                    )
                }
            }
            return _config!!
        }
}