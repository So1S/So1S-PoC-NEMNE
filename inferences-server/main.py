import bentoml
from bentoml.adapters import JsonInput
from bentoml.frameworks.transformers import TransformersModelArtifact
import numpy as np
from transformers import AutoModelForSequenceClassification, AutoTokenizer
import os

@bentoml.env(pip_packages=["transformers==4.11.0", "torch==1.7.0"])
@bentoml.artifacts([TransformersModelArtifact("transformer")])
class TransformerService(bentoml.BentoService):

    LABELS = {
        0: "부정",
        1: "긍정"
    }

    @bentoml.api(input=JsonInput(), batch=False)
    def predict(self, parsed_json):
        src_text = parsed_json.get("text")

        model = self.artifacts.transformer.get("model")
        tokenizer = self.artifacts.transformer.get("tokenizer")

        # Pre-processing
        input_ids = tokenizer.encode(src_text, return_tensors="pt")

        # Model forward
        output = model(input_ids)

        # Post-processing
        preds = output.logits.detach().cpu().numpy()
        preds = np.argmax(preds, axis=1)

        return {
            "result": self.LABELS[preds[0]]
        }

if __name__ == "__main__":
    ts = TransformerService()

    # model_name = "monologg/koelectra-small-finetuned-nsmc"
    # model = AutoModelForSequenceClassification.from_pretrained("monologg/koelectra-small-finetuned-nsmc")
    
    model_name = "./models/model.pt"
    model_name = os.environ.get("MODEL_NAME")
    model = AutoModelForSequenceClassification.from_pretrained(model_name)
    tokenizer = AutoTokenizer.from_pretrained("monologg/koelectra-small-finetuned-nsmc")
    artifact = {"model": model, "tokenizer": tokenizer}

    # 모델 저장하기
    # model.save_pretrained('./models/model.pt');

    ts.pack("transformer", artifact)
    saved_path = ts.save()