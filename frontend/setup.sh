#!/bin/bash

# NewRMS Frontend Setup Script
echo "🚀 Setting up NewRMS Frontend..."

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18+ first."
    exit 1
fi

# Check Node.js version
NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 18 ]; then
    echo "❌ Node.js version 18+ is required. Current version: $(node -v)"
    exit 1
fi

echo "✅ Node.js version: $(node -v)"

# Install dependencies
echo "📦 Installing dependencies..."
npm install

# Check if installation was successful
if [ $? -eq 0 ]; then
    echo "✅ Dependencies installed successfully!"
else
    echo "❌ Failed to install dependencies"
    exit 1
fi

# Create environment file if it doesn't exist
if [ ! -f .env ]; then
    echo "📝 Creating environment file..."
    cat > .env << EOL
REACT_APP_API_URL=http://localhost:8085/api
REACT_APP_ENV=development
GENERATE_SOURCEMAP=false
EOL
    echo "✅ Environment file created!"
fi

# Build the project to check for any issues
echo "🔨 Building project to verify setup..."
npm run build

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "🎉 Setup complete! You can now run:"
    echo "   npm start     - Start development server"
    echo "   npm run build - Build for production"
    echo "   npm test      - Run tests"
    echo ""
    echo "🌐 The app will be available at: http://localhost:3000"
else
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi