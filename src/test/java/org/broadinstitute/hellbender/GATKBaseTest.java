package org.broadinstitute.hellbender;

import htsjdk.samtools.SAMFileHeader;
import org.broadinstitute.hellbender.utils.GenomeLoc;
import org.broadinstitute.hellbender.utils.GenomeLocParser;
import org.broadinstitute.hellbender.utils.fasta.CachingIndexedFastaSequenceFile;
import org.broadinstitute.hellbender.utils.io.IOUtils;
import org.broadinstitute.hellbender.testutils.BaseTest;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This is the base test class for all of our test cases.  All test cases should extend from this
 * class; it sets up the logger, and resolves the location of directories that we rely on.
 */
public abstract class GATKBaseTest extends BaseTest {

    private static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
    public static final String gatkDirectory = System.getProperty("gatkdir", CURRENT_DIRECTORY) + "/";

    public static final String publicMainResourcesDir = new File(gatkDirectory, "src/main/resources").getAbsolutePath() + "/";
    public static final String packageMainResourcesDir = publicMainResourcesDir + "org/broadinstitute/hellbender/";

    private static final String publicTestDirRelative = "src/test/resources/";
    public static final String publicTestDir = new File(gatkDirectory, publicTestDirRelative).getAbsolutePath() + "/";
    public static final String publicTestDirRoot = publicTestDir.replace(publicTestDirRelative, "");

    public static final String packageRootTestDir = publicTestDir + "org/broadinstitute/hellbender/";
    public static final String toolsTestDir = packageRootTestDir + "tools/";
    public static final String exampleTestDir = toolsTestDir + "examples/";

    public static final String GCS_GATK_TEST_RESOURCES = "gs://hellbender/test/resources/";

    public static final String GCS_b37_REFERENCE_2BIT = GCS_GATK_TEST_RESOURCES + "benchmark/human_g1k_v37.2bit";
    public static final String GCS_b37_CHR20_21_REFERENCE_2BIT = GCS_GATK_TEST_RESOURCES + "large/human_g1k_v37.20.21.2bit";
    public static final String GCS_b37_CHR20_21_REFERENCE = GCS_GATK_TEST_RESOURCES + "large/human_g1k_v37.20.21.fasta";

    /**
     * LARGE FILES FOR TESTING (MANAGED BY GIT LFS)
     */
    public static final String largeFileTestDir = new File(publicTestDir, "large").getAbsolutePath() + "/";

    // All of chromosomes 20 and 21 from the b37 reference
    public static final String b37_reference_20_21 = largeFileTestDir + "human_g1k_v37.20.21.fasta";

    public static final String b37_2bit_reference_20_21 = largeFileTestDir + "human_g1k_v37.20.21.2bit";

    public static final String b37_reference_20_21_img = largeFileTestDir + "human_g1k_v37.20.21.fasta.img";

    // All of chromosomes 20 and 21 from the b38 reference
    public static final String b38_reference_20_21 = largeFileTestDir + "Homo_sapiens_assembly38.20.21.fasta";

    // ~600,000 reads from chromosomes 20 and 21 of an NA12878 WGS bam aligned to b37, plus ~50,000 unmapped reads
    public static final String NA12878_20_21_WGS_bam = largeFileTestDir + "CEUTrio.HiSeq.WGS.b37.NA12878.20.21.bam";
    public static final String NA12878_20_21_WGS_cram = largeFileTestDir + "CEUTrio.HiSeq.WGS.b37.NA12878.20.21.cram";

    // Variants from a DBSNP 138 VCF overlapping the reads in NA12878_20_21_WGS_bam
    public static final String dbsnp_138_b37_20_21_vcf = largeFileTestDir + "dbsnp_138.b37.20.21.vcf";

    // Variants from a DBSNP 138 VCF form the first 65Mb of chr1
    public static final String dbsnp_138_b37_1_65M_vcf = largeFileTestDir + "dbsnp_138.b37.1.1-65M.vcf";

    public static final String WGS_B37_CH20_1M_1M1K_BAM = "CEUTrio.HiSeq.WGS.b37.ch20.1m-1m1k.NA12878.bam";
    public static final String DBSNP_138_B37_CH20_1M_1M1K_VCF = "dbsnp_138.b37.excluding_sites_after_129.ch20.1m-1m1k.vcf";

    /**
     * END OF LARGE FILES FOR TESTING
     */

    public static final String NA12878_chr17_1k_BAM = publicTestDir + "NA12878.chr17_69k_70k.dictFix.bam";
    public static final String NA12878_chr17_1k_CRAM = publicTestDir + "NA12878.chr17_69k_70k.dictFix.cram";
    public static final String v37_chr17_1Mb_Reference = publicTestDir + "human_g1k_v37.chr17_1Mb.fasta";

    public static final String hg19_chr1_1M_Reference = publicTestDir + "Homo_sapiens_assembly19_chr1_1M.fasta";
    public static final String hg19_chr1_1M_dict = publicTestDir + "Homo_sapiens_assembly19_chr1_1M.dict";
    public static final String hg19_chr1_1M_dbSNP = publicTestDir + "Homo_sapiens_assembly19.dbsnp135.chr1_1M.exome_intervals.vcf";

    // the following file has been modified such that the first chromosome length is 1M; this is sometimes
    // required due to sequence dictionary validation, since a reference FASTA with only 1M bases is used
    public static final String hg19_chr1_1M_dbSNP_modified = publicTestDir + "HSA19.dbsnp135.chr1_1M.exome_intervals.modified.vcf";

    public static final String hg19_chr1_1M_exampleVCF = publicTestDir + "joint_calling.chr1_1M.1kg_samples.10samples.noINFO.vcf";
    public static final String hg19MiniReference = publicTestDir + "hg19mini.fasta";
    // Micro reference is the same as hg19mini, but contains only chromosomes 1 and 2
    public static final String hg19MicroReference = publicTestDir + "hg19micro.fasta";

    public static final String exampleFASTA = publicTestDir + "exampleFASTA.fasta";
    public static final String exampleReference = hg19MiniReference;
    public static final String hg19MiniIntervalFile = publicTestDir + "hg19mini.interval_list";
    public static final String wgsIntervalFile = publicTestDir + "wgs_calling_regions.v1.interval_list";

    public static final String DREAM_BAMS_DIR = publicTestDir + "large/mutect/dream_synthetic_bams";
    public static final String DREAM_VCFS_DIR = publicTestDir + "org/broadinstitute/hellbender/tools/mutect/dream/vcfs";

    public CachingIndexedFastaSequenceFile hg19ReferenceReader;
    public GenomeLocParser hg19GenomeLocParser;

    // used to seed the genome loc parser with a sequence dictionary
    protected SAMFileHeader hg19Header;

    @BeforeClass
    public void initGenomeLocParser() throws FileNotFoundException {
        hg19ReferenceReader = new CachingIndexedFastaSequenceFile(IOUtils.getPath(hg19MiniReference));
        hg19Header = new SAMFileHeader();
        hg19Header.setSequenceDictionary(hg19ReferenceReader.getSequenceDictionary());
        hg19GenomeLocParser = new GenomeLocParser(hg19ReferenceReader);
    }

    protected List<GenomeLoc> intervalStringsToGenomeLocs( String... intervals) {
        return intervalStringsToGenomeLocs(Arrays.asList(intervals));
    }

    protected List<GenomeLoc> intervalStringsToGenomeLocs( List<String> intervals ) {
        List<GenomeLoc> locs = new ArrayList<>();
        for (String interval: intervals)
            locs.add(hg19GenomeLocParser.parseGenomeLoc(interval));
        return Collections.unmodifiableList(locs);
    }
}

