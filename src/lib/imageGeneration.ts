import { buildIconifyPrompt, type IconifyPromptInput } from "./iconifyPrompt";

export type IconifyGenerationRequest = IconifyPromptInput & {
  logoFileName?: string;
};

export type IconifyGenerationResult = {
  prompt: string;
  status: "prompt-ready";
  message: string;
};

export function createIconifyGenerationRequest(
  request: IconifyGenerationRequest,
): IconifyGenerationResult {
  return {
    prompt: buildIconifyPrompt(request),
    status: "prompt-ready",
    message: request.logoFileName
      ? `Prompt prepared for ${request.logoFileName}. Connect an image model API to generate final assets.`
      : "Prompt prepared. Upload a logo and connect an image model API to generate final assets.",
  };
}
