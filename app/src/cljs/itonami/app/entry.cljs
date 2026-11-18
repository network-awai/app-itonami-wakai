(ns itonami.app.entry
  "Wakai on the device (AWAI-3502). Calls wakai.methods.pool unchanged:
  validation and aggregation only. `solve` (live pooling against member funds)
  is Council+operator gated in the repo and is not offered. No money moves."
  (:require [itonami.app.run :as run]
            [wakai.methods.pool :as pool]))

(run/register!
 {:name "contribution" :label "拠出の記録を検証する"
  :help "拠出の記録を作ります。investmentReturnPromised は入力できず、常に false です（投資ではない）。"
  :example "{:created-at \"2026-11-18T00:00:00Z\"
 :contributor-pseudonym-did \"did:key:z6Mk-example-member\"
 :contribution-amount-encrypted-cid \"bafy-example-amount\"
 :member-consent-cid \"bafy-example-consent\"
 :contribution-method \"in-kind-attested\"
 :ability-scaled-attested true}"
  :run pool/validate-contribution})

(run/register!
 {:name "distribution" :label "分配の記録を検証する"
  :help "分配の記録を作ります。共同体の識別と評議会の証明がそれぞれ 3 件未満なら退けます。claimAdjudicated は常に false（保険の査定ではない）です。"
  :example "{:created-at \"2026-11-18T00:00:00Z\"
 :recipient-pseudonym-did \"did:key:z6Mk-example-recipient\"
 :need-attestation-cid \"bafy-example-need\"
 :need-category \"disaster-emergency\"
 :community-discernment-attestations [\"att-1\" \"att-2\" \"att-3\"]
 :council-attestations [\"council-1\" \"council-2\" \"council-3\"]
 :distribution-method \"in-kind-coordination\"}"
  :run pool/validate-distribution})

(run/register!
 {:name "pool-state" :label "期間を集計する"
  :help "期間の拠出と分配を合計し、分類ごとの件数を出します。個人ごとの金額は結果に含めません。"
  :example "{:report-period-start-utc \"2026-10-01T00:00:00Z\"
 :report-period-end-utc \"2026-10-31T23:59:59Z\"
 :contributions [{:amount-usd-millicents 500000} {:amount-usd-millicents 250000}]
 :distributions [{:amount-usd-millicents 300000 :need-category \"disaster-emergency\"}]
 :council-attestations [\"council-1\" \"council-2\" \"council-3\"]}"
  :run pool/aggregate-pool-state})
