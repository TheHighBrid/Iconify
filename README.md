# Iconify

Iconify is a premium creative web app for turning flat logos into polished prompts for standalone, front-facing, inflated 3D app icons.

## Features

- Logo upload with instant local preview
- Brand-aware Iconify prompt generation
- Optional style notes for creative direction
- Prompt copy workflow for image-generation tools
- Placeholder result grid for future generated icon outputs

## Local Setup

```bash
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to use the studio.

## Project Structure

```text
src/app             Next.js App Router pages and layout
src/components      Upload, prompt, and result UI
src/lib             Prompt and generation request helpers
src/styles          Global Tailwind CSS entrypoint
public              Static assets
```

## Image Generation Roadmap

Iconify currently prepares high-quality prompts and logo context. The next milestone is connecting `src/lib/imageGeneration.ts` to an image-generation API so the app can return downloadable icon assets directly.
