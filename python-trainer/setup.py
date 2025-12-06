from setuptools import setup, find_packages

setup(
    name='factory-ml-offline-system',
    version='0.1.0',
    author='Your Name',
    author_email='your.email@example.com',
    description='A machine learning system for structured data in an offline factory environment.',
    packages=find_packages(where='src'),
    package_dir={'': 'src'},
    install_requires=[
        'scikit-learn',
        'pandas',
        'onnxruntime',
        'shap',
        'evidently',
        'dtreeviz'
    ],
    entry_points={
        'console_scripts': [
            'trainer=trainer.main:main',
        ],
    },
)